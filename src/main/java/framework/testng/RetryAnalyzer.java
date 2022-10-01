package framework.testng;

import com.bigbasket.automation.BaseSettings;
import org.testng.IAnnotationTransformer;
import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;
import org.testng.annotations.ITestAnnotation;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class RetryAnalyzer implements IRetryAnalyzer, IAnnotationTransformer {

    private static final int MAX_RETRY_COUNT = 1;

    private final Map<Integer, AtomicInteger> counts = new HashMap<>();

    @Override
    public void transform(ITestAnnotation annotation, Class testClass, Constructor testConstructor, Method testMethod) {
        IRetryAnalyzer retry = annotation.getRetryAnalyzer();
        if (retry == null)
            annotation.setRetryAnalyzer(RetryAnalyzer.class);
    }

    private AtomicInteger getCount(ITestResult result) {
        int id = Arrays.hashCode(result.getParameters());
        AtomicInteger count = counts.get(id);
        if (count == null) {
            count = new AtomicInteger(MAX_RETRY_COUNT);
            counts.put(id, count);
        }
        return count;
    }

    @Override
    public boolean retry(ITestResult result) {
        int retriesRemaining = getCount(result).getAndDecrement();
        System.out.println(String.format(
                "Retries remaining for test '%s' with params '%s': %d",
                result.getMethod().getMethodName(),
                Arrays.toString(result.getParameters()), retriesRemaining));
        if (BaseSettings.retryMode)
            return retriesRemaining > 0;
        else return false;
    }
}