package HubopsPages;

import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import java.text.DateFormatSymbols;
import java.text.ParseException;
import java.util.List;
import java.util.Map;

public class HubopsPage {
    @FindBy(xpath = "//i[text()='Add']/..")
    WebElement addButton;
    @FindBy(xpath = "//button[text()='Submit']")
    WebElement submitButton;
    private WebDriver driver;
    private WebDriverWait wait;
    @FindBy(linkText = "Transport Management System")
    private WebElement getTransportManagementSystemLink;

    private WebElement getModuleElement(String moduleName) {
        return driver.findElement(By.xpath("//span[text()='" + moduleName + "']/.."));
    }

    private WebElement getSubModuleElement(String subModuleName) {
        return driver.findElement(By.xpath("//a[text()='" + subModuleName + "']"));
    }

    private WebElement getAppVersionElement() {
        return driver.findElement(By.id("/app_version"));
    }

    private List<WebElement> getInputFields() {
        return driver.findElements(By.xpath("//td"));
    }

    ///////***********Field Assets ShiftType Elements************//////////

    private WebElement getName() {
        return driver.findElement(By.id("name"));
    }

    private WebElement getSlug() {
        return driver.findElement(By.id("slug"));
    }

    private WebElement getDropdownJobType() {
        return driver.findElement(By.id("job_type_chosen"));
    }

    private WebElement getNumberofhours() {
        return driver.findElement(By.name("number_of_hours"));
    }

    private WebElement getRadioBtnIsActive() {
        return driver.findElement(By.name("is_active"));
    }

    private WebElement messageValidation() {
        return driver.findElement(By.id("messages"));
    }

    ///////***********Field Assets Cee Work Type Elements************//////////

    private WebElement getWorkType() {
        return driver.findElement(By.id("work_type"));
    }

    private WebElement getRadioBtnIsActive1() {
        return driver.findElement(By.xpath("//input[@name='is_active']"));
    }


    ///////***********Field Assets Cee Work Type Elements************//////////
    private WebElement getCategory() {
        return driver.findElement(By.id("category"));
    }

    private WebElement getCheckBoxIsActive() {
        return driver.findElement(By.xpath("//input[@name='is_active']"));
    }

    ///////***********Field Assets Earning Type Elements************//////////
    private WebElement getCategory1() {
        return driver.findElement(By.id("category"));
    }

    private WebElement getCheckBoxIsActive1() {
        return driver.findElement(By.xpath("//input[@name='is_active']"));
    }

    ///////***********LMD Providers Elements************//////////
    private WebElement getName1() {
        return driver.findElement(By.id("name"));
    }

    private WebElement getShortname() {
        return driver.findElement(By.id("short_name"));
    }

    private WebElement getScopeType() {
        return driver.findElement(By.id("scope_type"));
    }

    private WebElement getAgreementRenewalDate() {
        return driver.findElement(By.name("agreement_renewal_date"));
    }

    private WebElement getMonthYearText() {
        return driver.findElement(By.xpath("//div[@class='datepicker-days']//table//tr[2]//th[2]"));
    }

    private WebElement getNextButton() {
        return driver.findElement(By.xpath("//div[@class='datepicker-days']//table//tr[2]//th[3]"));
    }

    private WebElement getRequiredDateOption(int date) {
        return driver.findElement(By.xpath("//td[text()='" + date + "']"));
    }

    //div[@class='datepicker-days']//table//tr[2]//th[2]

    private WebElement getHQAddress() {
        return driver.findElement(By.name("hq_address"));
    }

    private WebElement getAccountManager1() {
        return driver.findElement(By.id("account_manager"));
    }

    private WebElement getContactNumber() {
        return driver.findElement(By.name("contact_number"));
    }

    private WebElement getBankAccountDetails() {
        return driver.findElement(By.name("bank_account_details"));
    }

    private WebElement getTripCallbackUrl() {
        return driver.findElement(By.id("trip_callback_url"));
    }

    private WebElement getCheckBoxIsActive2() {
        return driver.findElement(By.xpath("//input[@name='is_active']"));
    }

    private WebElement getOperatingType() {
        return driver.findElement(By.id("operating_type"));
    }

    private WebElement getBBSignAPIKey() {
        return driver.findElement(By.id("bb_sign_api_key"));
    }

    private WebElement getAPIDetails() {
        return driver.findElement(By.id("api_details"));
    }

    ///////***********Field Assets Vehicle Type Elements************//////////
    private WebElement getname2() {
        return driver.findElement(By.id("name"));
    }

    private WebElement getCapacity() {
        return driver.findElement(By.id("capacity"));
    }

    private WebElement getMaxWeight() {
        return driver.findElement(By.id("max_weight"));
    }

    /*private WebElement getDeliveryMode() {
        return driver.findElement(By.id("dm_id_chosen"));
    }*/
    private WebElement getLength() {
        return driver.findElement(By.id("length"));
    }

    private WebElement getBreadth() {
        return driver.findElement(By.id("breadth"));
    }

    private WebElement getHeight() {
        return driver.findElement(By.id("height"));
    }

    //////**************EC Group Mapping Elements**********///////
    private WebElement getECGroupDropDown() {
        return driver.findElement(By.id("ec_group_id_chosen"));
    }

    private WebElement getEcIds() {
        return driver.findElement(By.id("ec_ids_chosen"));
    }

    private WebElement getCheckBoxIsActive3() {
        return driver.findElement(By.xpath("//input[@name='is_active']"));
    }

    ///////***********Global DM Config Elements************//////////
    private WebElement getDeliveryMode() {
        return driver.findElement(By.id("dm_id_chosen"));
    }

    private WebElement getVehicleType() {
        return driver.findElement(By.id("vehicle_type_id_chosen"));
    }

    private WebElement getServiceTime() {
        return driver.findElement(By.name("service_time"));
    }

    private WebElement getServiceTimeLinkedOrder() {
        return driver.findElement(By.name("service_time_linked_order"));
    }

    private WebElement getMinRoutingOrderThreshold() {
        return driver.findElement(By.id("min_routing_order_threshold"));
    }

    private WebElement getMaxRoutingOrderThreshold() {
        return driver.findElement(By.id("max_routing_order_threshold"));
    }

    private WebElement getDispatchTimeOffsetfortheSlots() {
        return driver.findElement(By.name("dispatch_time_offset_for_the_slots"));
    }

    private WebElement getDispatchTimeOffsetfortheNowSlots() {
        return driver.findElement(By.name("dispatch_time_offset_for_the_now_slots"));
    }

    private WebElement getSlotEndGraceTimeforUnack() {
        return driver.findElement(By.name("slot_end_grace_time_for_unack_orders"));
    }

    private WebElement getSendFullCompartmentDetails() {
        return driver.findElement(By.id("send_full_compartment_details_to_route_provider_chosen"));
    }

    private WebElement getClusteringMode() {
        return driver.findElement(By.id("clustering_mode_chosen"));
    }

    private WebElement getModeofRouting() {
        return driver.findElement(By.id("mode_of_routing_chosen"));
    }

    private WebElement getDynamicVehicleConf() {
        return driver.findElement(By.id("is_dynamic_vehicle_conf_enabled_chosen"));
    }

    private WebElement getDynamicVehicleConfig() {
        return driver.findElement(By.id("dynamic_vehicle_conf"));
    }

    private WebElement getAdditionalConfig() {
        return driver.findElement(By.name("additional_config"));
    }

    ////*************Global Hubops EC Group Config*************//////
    private WebElement getECGroupIdDropDown() {
        return driver.findElement(By.id("ec_group_id_chosen"));
    }

    private WebElement getMapView() {
        return driver.findElement(By.name("map_percent"));
    }//*[@class="form-control"]

    private WebElement getVipOrderPriority() {
        return driver.findElement(By.id("locus_vip_order_priority"));
    }

    private WebElement getSlotBuffer() {
        return driver.findElement(By.id("locus_slot_buffer"));
    }

    private WebElement getServiceTimeConfig() {
        return driver.findElement(By.id("service_time_config"));
    }

    private WebElement getEnableBatching() {
        return driver.findElement(By.id("enable_batching_for_now_slots_chosen"));
    }

    private WebElement getDeliveryFlowChosen() {
        return driver.findElement(By.id("delivery_flow_chosen"));
    }

    private WebElement getAddConfig() {
        return driver.findElement(By.id("additional_config"));
    }

    ///////***********Global SA Type Config Elements************//////////

    private WebElement getSAType() {
        return driver.findElement(By.id("satype_id_chosen"));
    }

    private WebElement getInternalBtwnRoutn() {
        return driver.findElement(By.id("interval_between_routing"));
    }

    private WebElement getLatestRouteAssgn() {
        return driver.findElement(By.name("latest_route_assignment_eligibility"));
    }

    private WebElement getNoofConsicWindAssig() {
        return driver.findElement(By.name("number_of_consecutive_windows_assignment"));
    }

    private WebElement getMaxWaitTime() {
        return driver.findElement(By.id("max_wait_time_for_routing"));
    }

    private WebElement getMaxFractionOfOrdrs() {
        return driver.findElement(By.name("max_fraction_of_orders_to_merge_in_manual"));
    }

    private WebElement getMaxWaitTimeRoutin() {
        return driver.findElement(By.name("max_wait_time_for_routing"));
    }

    private WebElement getMaxFracOfOrdrsMergeinManual() {
        return driver.findElement(By.name("max_fraction_of_orders_to_merge_in_manual"));
    }

    private WebElement getDefaultVehicleOffset() {
        return driver.findElement(By.name("default_vehicle_operating_offset"));
    }

    private WebElement getDispatchWinInterval() {
        return driver.findElement(By.name("dispatch_window_interval"));
    }

    private WebElement getDispatchWinIntervalLimit() {
        return driver.findElement(By.name("dispatch_window_duration_limit"));
    }

    private WebElement getDeliveryAppVersion() {
        return driver.findElement(By.id("dapp_id_chosen"));
    }

    private WebElement getIsSkipByoc() {
        return driver.findElement(By.id("is_skip_by_oc_enabled_chosen"));
    }

    private WebElement getEarlyDelAllowed() {
        return driver.findElement(By.id("is_early_delivery_allowed_chosen"));
    }

    private WebElement getdelayNotEnabldChosen() {
        return driver.findElement(By.id("is_delay_notification_enabled_chosen"));
    }

    private WebElement getDapiShortCollConfig() {
        return driver.findElement(By.id("dapi_short_collection_config"));
    }

    private WebElement getCheckInAccDistance() {
        return driver.findElement(By.name("check_in_acceptable_distance"));
    }

    private WebElement getCheckInDeltaTime() {
        return driver.findElement(By.name("check_in_delta_time_minutes"));
    }

    private WebElement getCommBuffTimeMin() {
        return driver.findElement(By.name("comm_buffer_time_minutes"));
    }

    private WebElement getEtaBuffrTimeMin() {
        return driver.findElement(By.name("eta_buffer_time_minutes"));
    }

    private WebElement getEtaMinMaxConfig() {
        return driver.findElement(By.id("eta_min_max_config"));
    }

    private WebElement getDnSilWindow() {
        return driver.findElement(By.name("dn_silent_window"));
    }

    private WebElement getdapiCallEretail() {
        return driver.findElement(By.id("dapi_call_eretail_for_crate_info_chosen"));
    }

    private WebElement getDapiGrpLinkdOrdr() {
        return driver.findElement(By.id("dapi_group_linked_orders_chosen"));
    }

    private WebElement getDelayDelTmeMin() {
        return driver.findElement(By.name("delay_delta_time_minutes"));
    }

    private WebElement getgraceDelTimeMin() {
        return driver.findElement(By.name("grace_delivery_time_minutes"));
    }

    private WebElement getSkinActions() {
        return driver.findElement(By.id("skip_actions"));
    }

    private WebElement getSkipCallBuffer() {
        return driver.findElement(By.name("skip_call_buffer"));
    }

    private WebElement getWarningperiod() {
        return driver.findElement(By.name("warning_period"));
    }

    private WebElement getEnableEq() {
        return driver.findElement(By.id("enable_eq_chosen"));
    }

    private WebElement getEnableCeepayloadChosn() {
        return driver.findElement(By.id("enable_sync_cee_device_payload_chosen"));
    }

    private WebElement getDelTypeLable() {
        return driver.findElement(By.id("delivery_type_label"));
    }

    private WebElement getdapiDateSelEnableChosen() {
        return driver.findElement(By.id("dapi_date_selection_enable_config_chosen"));
    }

    private WebElement getAddnalConfigg() {
        return driver.findElement(By.id("additional_config"));
    }

    //////**************HUbOps EC Group Elements**********///////
    private WebElement getECGroupName() {
        return driver.findElement(By.id("group_name"));
    }

    private WebElement getDescription() {
        return driver.findElement(By.id("description"));
    }

    private List<WebElement> getInputFields1() {
        return driver.findElements(By.xpath("//td"));
    }

    ///////***********Global SA DM Config ************//////////
    private WebElement getSAID() {
        return driver.findElement(By.id("sa_id_chosen"));
    }

    private WebElement getDeliveryMode1() {
        return driver.findElement(By.id("dm_id_chosen"));
    }
    private WebElement getVehicleType1() {
        return driver.findElement(By.id("vehicle_type_id_chosen"));
    }

    private WebElement getMailingList() {
        return driver.findElement(By.name("mailing_list"));
    }
    private WebElement getMaxWaitTimeforRouting() {
        return driver.findElement(By.id("max_wait_time_for_routing"));
    }

    private WebElement getIntervalBetnRouting() {
        return driver.findElement(By.id("interval_between_routing"));
    }
    private WebElement getMaxFracnOfOrderstoMergeinManual() {
        return driver.findElement(By.name("max_fraction_of_orders_to_merge_in_manual"));
    }
    private WebElement getPrimaryContactNo() {
        return driver.findElement(By.name("primary_contact_no"));
    }
    private WebElement getSecondaryContactNo() {
        return driver.findElement(By.name("secondary_contact_no"));
    }
    private WebElement getTertiaryContactNo() {
        return driver.findElement(By.name("tertiary_contact_no"));
    }
    private WebElement getAlternateContactNo() {
        return driver.findElement(By.name("alternate_contact_no"));
    }
    private WebElement getDispatchTimeOffsetfortheSlots1() {
        return driver.findElement(By.name("dispatch_time_offset_for_the_slots"));
    }
    private WebElement getDispatchTimeOffsetfortheNowSlots1() {
        return driver.findElement(By.name("dispatch_time_offset_for_the_now_slots"));
    }
    private WebElement getLatestRouteAssignmentEligibility() {
        return driver.findElement(By.name("latest_route_assignment_eligibility"));
    }
    private WebElement getNumofConsWinsAssign() {
        return driver.findElement(By.name("number_of_consecutive_windows_assignment"));
    }
    private WebElement getRoutingConfig() {
        return driver.findElement(By.id("routing_conf_id_chosen"));
    }
    private WebElement getMapProvider() {
        return driver.findElement(By.id("map_provider_chosen"));
    }

    private WebElement getCSContact() {
        return driver.findElement(By.name("cs_contact"));
    }
    private WebElement getConferenceDID() {
        return driver.findElement(By.name("conference_did"));
    }
    private WebElement getIsDappPilot() {
        return driver.findElement(By.id("is_dapp_pilot_chosen"));
    }
    private WebElement getDappPilotCEEList() {
        return driver.findElement(By.name("dapp_pilot_cee_list"));
    }
    private WebElement getDeliverAppVersion() {
        return driver.findElement(By.id("dapp_id_chosen"));
    }

    private WebElement getIsSkipbyOCEnabled() {
        return driver.findElement(By.id("is_skip_by_oc_enabled_chosen"));
    }
    private WebElement getIsEarlyDeliveryAllowed() {
        return driver.findElement(By.id("is_early_delivery_allowed_chosen"));
    }

    private WebElement getIsDelayNotifEnabled() {
        return driver.findElement(By.id("is_delay_notification_enabled_chosen"));
    }
    private WebElement getEnableDelPhotos() {
        return driver.findElement(By.id("enable_delivery_photos_chosen"));
    }
    private WebElement getEnableCusttoCEECalls() {
        return driver.findElement(By.id("enable_customer_to_cee_calls_chosen"));
    }

    private WebElement getAllowAssignfromUI() {
        return driver.findElement(By.id("allow_assignment_from_ui_chosen"));
    }
    private WebElement getAllowQRScanCheckIn() {
        return driver.findElement(By.id("allow_qr_scan_checkin_chosen"));
    }
    private WebElement getLocSyncInterval() {
        return driver.findElement(By.name("location_sync_interval"));
    }
    private WebElement getBinScanDelta() {
        return driver.findElement(By.name("bin_scan_delta"));
    }
    private WebElement getTripLocSyncInterval() {
        return driver.findElement(By.name("trip_location_sync_interval"));
    }
    private WebElement getCommBufferTimeMin() {
        return driver.findElement(By.name("comm_buffer_time_minutes"));
    }
    private WebElement getETABufferTimeMin() {
        return driver.findElement(By.name("eta_buffer_time_minutes"));
    }
    private WebElement getETAMinMaxConfig() {
        return driver.findElement(By.name("eta_min_max_config"));
    }
    private WebElement getDelayDeltaTimeMin() {
        return driver.findElement(By.name("delay_delta_time_minutes"));
    }
    private WebElement getDNSilentWindow() {
        return driver.findElement(By.name("dn_silent_window"));
    }
    private WebElement getRecEmailIdsforDelExcReport() {
        return driver.findElement(By.name("recipient_email_ids"));
    }
    private WebElement getModeOfRouting() {
        return driver.findElement(By.id("mode_of_routing_chosen"));
    }
    private WebElement getIsDynamicVehicleConfEnabled() {
        return driver.findElement(By.id("is_dynamic_vehicle_conf_enabled_chosen"));
    }
    private WebElement getDynamicVehicleConf1() {
        return driver.findElement(By.id("dynamic_vehicle_conf"));
    }

    private WebElement getSendFullCompartDetailsToRouteProvider() {
        return driver.findElement(By.id("send_full_compartment_details_to_route_provider_chosen"));
    }
    private WebElement getMinDrivingTime() {
        return driver.findElement(By.name("min_driving_time"));
    }
    private WebElement getAdditionalConfig1() {
        return driver.findElement(By.id("additional_config"));
    }
    ///////***********Global SA DM Provider Config ************//////////

    private WebElement getRoutingProvider() {
        return driver.findElement(By.id("routing_provider_id_chosen"));
    }

    private WebElement getPriority() {
        return driver.findElement(By.name("priority"));
    }
    private WebElement getProfile() {
        return driver.findElement(By.id("profile_chosen"));
    }

    private WebElement getVehicleUsageFactor() {
        return driver.findElement(By.id("vehicle_usage_factor_chosen"));
    }
    private WebElement getAdditionalConfig2() {
        return driver.findElement(By.name("additional_config"));
    }
    ///////***********Delivery APP Version Elements************//////////
    private WebElement getAppVersion() {
        return driver.findElement(By.id("app_version"));
    }

    private WebElement getApkFile() {
        return driver.findElement(By.id("apk_file"));
    }

    private WebElement getCheckSum() {
        return driver.findElement(By.id("check_sum"));
    }

    private WebElement getAdditionalData() {
        return driver.findElement(By.id("additional_values"));
    }
    public HubopsPage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
        wait = new WebDriverWait(driver, 60);
    }

    public void scrollIntoView(WebElement element) {
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", element);
    }

    public void enterHubopsDetails(String moduleName, String subModuleName, List<List<String>> allDetails) throws InterruptedException {
        scrollIntoView(getTransportManagementSystemLink);
        Thread.sleep(5000);
        getTransportManagementSystemLink.click();
        wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//span[text()='" + moduleName + "']/..")));
        wait.until(ExpectedConditions.elementToBeClickable(getModuleElement(moduleName)));
        getModuleElement(moduleName).click();
        wait.until(ExpectedConditions.elementToBeClickable(getSubModuleElement(subModuleName)));
        getSubModuleElement(subModuleName).click();
        System.out.println("number of rows are" + allDetails.size());
        for (List<String> details : allDetails) {
            Thread.sleep(5000);
            addButton.click();
            List<WebElement> inputElements = getInputFields();
            for (int i = 0; i < details.size(); i++) {
                if (details.get(i).equals("")) {
                    inputElements.get(i).sendKeys(".");
                } else {
                    try {
                        inputElements.get(i).findElement(By.xpath(".//div")).click();
                        Actions actions = new Actions(driver);
                        actions.sendKeys(details.get(i));
                        actions.sendKeys(Keys.ENTER).build().perform();
                    } catch (Exception ex) {
                        inputElements.get(i).findElement(By.xpath(".//input")).sendKeys(details.get(i));
                    }
                }
            }
            submitButton.click();
        }
    }

    public void hubopsShiftType(Map<String, String> testData) {
        addButton.click();
        getName().sendKeys(testData.get("Name"));
        getSlug().sendKeys(testData.get("Slug"));
        getDropdownJobType().click();
        Actions actions = new Actions(driver);
        actions.sendKeys(testData.get("Job Type"));
        actions.sendKeys(Keys.ENTER).build().perform();
        getNumberofhours().sendKeys(testData.get("Number of hours"));
        boolean status = getRadioBtnIsActive().isSelected();
        if (testData.get("Is Active").equals("Yes")) {
            if (!status) {
                getRadioBtnIsActive().click();
            }
        } else {
            if (status) {
                getRadioBtnIsActive().click();
            }
        }
        actions.click();
        submitButton.click();
        String message = messageValidation().getText();
        boolean messageValue = message.contains("created successfully") || message.contains("already exists");
        Assert.assertEquals(messageValue, true, "Message is Displayed");
    }

    public void hubopsCeeWorkType(Map<String, String> testData) throws InterruptedException {
        addButton.click();
        WebDriverWait wait = new WebDriverWait(driver, 30);
        getWorkType().sendKeys(testData.get("WorkType"));
        wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//input[@name='is_active']")));
        boolean status = getRadioBtnIsActive1().isSelected();
        if (testData.get("Is Active").equals("Yes")) {
            if (!status) {
                getRadioBtnIsActive1().click();
            }
        } else {
            if (status) {
                getRadioBtnIsActive1().click();
            }
        }
        submitButton.click();
        String message = messageValidation().getText();
        boolean messageValue = message.contains("created successfully") || message.contains("already exists");
        Assert.assertEquals(messageValue, true, "Message is Displayed");
    }

    public void hubopsCeeCategory(Map<String, String> testData) throws InterruptedException {
        addButton.click();
        WebDriverWait wait = new WebDriverWait(driver, 30);
        getCategory().sendKeys(testData.get("Category"));
        wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//input[@name='is_active']")));
        boolean status = getCheckBoxIsActive().isSelected();
        if (testData.get("Is Active").equals("Yes")) {
            if (!status) {
                getCheckBoxIsActive().click();
            }
        } else {
            if (status) {
                getCheckBoxIsActive().click();
            }
        }
        submitButton.click();
        String message = messageValidation().getText();
        boolean messageValue = message.contains("created successfully") || message.contains("already exists");
        Assert.assertEquals(messageValue, true, "Message is Displayed");
    }

    public void hubopsEarningType(Map<String, String> testData) throws InterruptedException {
        addButton.click();
        WebDriverWait wait = new WebDriverWait(driver, 30);
        getCategory1().sendKeys(testData.get("Category"));
        wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//input[@name='is_active']")));
        boolean status = getCheckBoxIsActive1().isSelected();
        if (testData.get("Is Active").equals("Yes")) {
            if (!status) {
                getCheckBoxIsActive1().click();
            }
        } else {
            if (status) {
                getCheckBoxIsActive1().click();
            }
        }
        submitButton.click();
        String message = messageValidation().getText();
        boolean messageValue = message.contains("created successfully") || message.contains("already exists");
        Assert.assertEquals(messageValue, true, "Message is Displayed");
    }

    public void hubopsLmdProvider(Map<String, String> testData) throws ParseException, InterruptedException {
        addButton.click();
        getName1().sendKeys(testData.get("Name"));
        getShortname().sendKeys(testData.get("Short Name"));
        getScopeType().click();
        Actions actions = new Actions(driver);
        actions.sendKeys(testData.get("Scope Type"));
        actions.sendKeys(Keys.ENTER).build().perform();
        getAgreementRenewalDate().click();
        String requiredDate = testData.get("Agreement Renewal Date");
        String[] splitDate = requiredDate.split("/");
        String monthString = new DateFormatSymbols().getMonths()[Integer.parseInt(splitDate[1]) - 1];
        String monthYear = monthString + " " + splitDate[2];
        System.out.println(monthYear);
        while (!monthYear.equalsIgnoreCase(getMonthYearText().getText().trim())) {
            getNextButton().click();
        }
        getRequiredDateOption(Integer.parseInt(splitDate[0])).click();
        getHQAddress().sendKeys(testData.get("HQ Address"));
        getAccountManager1().sendKeys(testData.get("Account Manager"));
        getContactNumber().sendKeys(testData.get("Contact Number"));
        getBankAccountDetails().sendKeys(testData.get("Bank Account Details"));
        getTripCallbackUrl().sendKeys(testData.get("Trip Callback Url"));
        boolean status = getCheckBoxIsActive2().isSelected();
        if (testData.get("Is Active").equals("Yes")) {
            if (!status) {
                getCheckBoxIsActive2().click();
            }
        } else {
            if (status) {
                getCheckBoxIsActive2().click();
            }
        }
        actions.click();
        Thread.sleep(2000);
        Select dd = new Select(getOperatingType());
        dd.selectByVisibleText(testData.get("Operating Type"));
        getBBSignAPIKey().sendKeys(testData.get("BB Sign API Key"));
        getAPIDetails().clear();
        getAPIDetails().sendKeys(testData.get("API Details"));
        submitButton.click();
        String message = messageValidation().getText();
        boolean messageValue = message.contains("created successfully") || message.contains("already taken");
        Assert.assertEquals(messageValue, true, "Message is Displayed");
    }

    public void hubopsVehicleType(Map<String, String> testData) throws InterruptedException {
        addButton.click();
        getname2().sendKeys(testData.get("Name"));
        getCapacity().sendKeys(testData.get("Capacity"));
        getMaxWeight().sendKeys(testData.get("Max weight"));
        Actions actions = new Actions(driver);
        getDeliveryMode().click();
        if (testData.get("Delivery Mode").equals("1"))
            actions.sendKeys("Van");
        else if (testData.get("Delivery Mode").equals("2"))
            actions.sendKeys("Bike");
        actions.sendKeys(Keys.ENTER).build().perform();
        getLength().sendKeys(testData.get("Length"));
        getBreadth().sendKeys(testData.get("Breadth"));
        getHeight().sendKeys(testData.get("Height"));
        boolean status = getCheckBoxIsActive2().isSelected();
        if (testData.get("Is Active").equals("Yes")) {
            if (!status) {
                getCheckBoxIsActive2().click();
            }
        } else {
            if (status) {
                getCheckBoxIsActive2().click();
            }
        }
        actions.click();
        Thread.sleep(2000);
        submitButton.click();
        String message = messageValidation().getText();
        boolean messageValue = message.contains("created successfully") || message.contains("already exists");
        Assert.assertEquals(messageValue, true, "Message is Displayed");
    }

    public void hubopsVehicleCategory(Map<String, String> testData) throws InterruptedException {
        addButton.click();
        WebDriverWait wait = new WebDriverWait(driver, 30);
        getCategory1().sendKeys(testData.get("Category"));
        wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//input[@name='is_active']")));
        boolean status = getCheckBoxIsActive1().isSelected();
        if (testData.get("Is Active").equals("Yes")) {
            if (!status) {
                getCheckBoxIsActive1().click();
            }
        } else {
            if (status) {
                getCheckBoxIsActive1().click();
            }
        }
        submitButton.click();
        String message = messageValidation().getText();
        boolean messageValue = message.contains("created successfully") || message.contains("already exists");
        Assert.assertEquals(messageValue, true, "Message is Displayed");
    }

    public void ecGroupMapping(Map<String, String> testData) {
        addButton.click();
        getECGroupDropDown().click();
        Actions actions = new Actions(driver);
        actions.sendKeys(testData.get("EC Group"));
        actions.sendKeys(Keys.ENTER).build().perform();
        getEcIds().click();
        actions.sendKeys(testData.get("Entry Contexts"));
        actions.sendKeys(Keys.ENTER).build().perform();
        submitButton.click();
        String message = messageValidation().getText();
        boolean messageValue = message.contains("created successfully") || message.contains("already exists");
        Assert.assertEquals(messageValue, true, "Message is Displayed");

    }

    public void globalDMConfig(Map<String, String> testData) throws InterruptedException {
        addButton.click();
        getDeliveryMode().click();
        Actions actions = new Actions(driver);
        actions.sendKeys(testData.get("Delivery Mode"));
        actions.sendKeys(Keys.ENTER).build().perform();
        getVehicleType().click();
        actions.sendKeys(testData.get("Vehicle Type"));
        actions.sendKeys(Keys.ENTER).build().perform();
        getServiceTime().sendKeys(testData.get("Service Time"));
        getServiceTimeLinkedOrder().sendKeys(testData.get("Service Time Linked Order"));
        getMinRoutingOrderThreshold().sendKeys(testData.get("Min Routing Order Threshold"));
        Thread.sleep(2000);
        getMaxRoutingOrderThreshold().sendKeys(testData.get("Max Routing Order Threshold"));
        getDispatchTimeOffsetfortheSlots().sendKeys(testData.get("Dispatch Time Offset for the Slots"));
        getDispatchTimeOffsetfortheNowSlots().sendKeys(testData.get("Dispatch Time Offset for the Now Slots"));
        getSlotEndGraceTimeforUnack().sendKeys(testData.get("Slot End Grace Time for Unack Orders"));
        getSendFullCompartmentDetails().click();
        Thread.sleep(2000);
        if (testData.get("Send Full Compartment Details to Route Provider").equals("False"))
            actions.sendKeys("No");
        else if (testData.get("Send Full Compartment Details to Route Provider").equals("True"))
            actions.sendKeys("Yes");
        actions.sendKeys(Keys.ENTER).build().perform();
        getClusteringMode().click();
        actions.sendKeys(testData.get("Clustering Mode"));
        actions.sendKeys(Keys.ENTER).build().perform();
        getModeofRouting().click();
        actions.sendKeys(testData.get("Mode of Routing"));
        actions.sendKeys(Keys.ENTER).build().perform();
        getDynamicVehicleConf().click();
        actions.sendKeys(testData.get("Dynamic Vehicle Conf Enabled"));
        actions.sendKeys(Keys.ENTER).build().perform();
        getDynamicVehicleConfig().sendKeys(testData.get("Dynamic Vehicle Config"));
        getAdditionalConfig().sendKeys(testData.get("Additional Config"));
        Thread.sleep(10000);
        submitButton.click();
        String message = messageValidation().getText();
        boolean messageValue = message.contains("created successfully") || message.contains("already exists");
        Assert.assertEquals(messageValue, true, "Message is Displayed");

    }

    public void globalHubopsECGroupConfig(Map<String, String> testData) throws InterruptedException {
        addButton.click();
        getECGroupIdDropDown().click();
        Actions actions = new Actions(driver);
        actions.sendKeys(testData.get("EC Group"));
        actions.sendKeys(Keys.ENTER).build().perform();
        getMapView().sendKeys(testData.get("Default Map View Percentage"));
        getVipOrderPriority().sendKeys(testData.get("Vip Order Priority"));
        getSlotBuffer().sendKeys(testData.get("Slot Buffer"));
        getServiceTimeConfig().sendKeys(testData.get("Service Time Config"));
        getEnableBatching().click();
        actions.sendKeys(testData.get("Enable Batching For Now Slots"));
        actions.sendKeys(Keys.ENTER).build().perform();
        getDeliveryFlowChosen().click();
        actions.sendKeys(testData.get("Delivery Flow"));
        actions.sendKeys(Keys.ENTER).build().perform();
        Thread.sleep(3000);
        getAddConfig().click();
        getAddConfig().sendKeys(testData.get("Additional Config"));
        submitButton.click();
        String message = messageValidation().getText();
        boolean messageValue = message.contains("created successfully") || message.contains("already exists");
        Assert.assertEquals(messageValue, true, "Message is Displayed");

    }

    public void globalSATypeConfig(Map<String, String> testData) throws InterruptedException {
        addButton.click();
        getSAType().click();
        Actions actions = new Actions(driver);
        actions.sendKeys(testData.get("SA Type"));
        actions.sendKeys(Keys.ENTER).build().perform();
        getInternalBtwnRoutn().sendKeys(testData.get("Interval Between Routing"));
        getLatestRouteAssgn().sendKeys(testData.get("Latest Route Assignment Eligibility"));
        getNoofConsicWindAssig().sendKeys(testData.get("Number of Consecutive Windows Assignment"));
        getMaxWaitTime().sendKeys(testData.get("Max Wait Time for Routing"));
        Thread.sleep(2000);
        getMaxFractionOfOrdrs().sendKeys(testData.get("Max Fraction of Orders to Merge in Manual"));
        getDefaultVehicleOffset().sendKeys(testData.get("Default Vehicle Operating Offset"));
        getDispatchWinInterval().sendKeys(testData.get("Dispatch Window Interval"));
        getDispatchWinIntervalLimit().sendKeys(testData.get("Dispatch Window Duration Limit"));
        Thread.sleep(2000);
        getDeliveryAppVersion().click();
        actions.sendKeys(testData.get("Delivery App Version"));
        actions.sendKeys(Keys.ENTER).build().perform();
        getIsSkipByoc().click();
        actions.sendKeys(testData.get("Is Skip by OC Enabled"));
        actions.sendKeys(Keys.ENTER).build().perform();
        Thread.sleep(2000);
        getEarlyDelAllowed().click();
        actions.sendKeys(testData.get("Is Early Delivery Allowed"));
        actions.sendKeys(Keys.ENTER).build().perform();
        getdelayNotEnabldChosen().click();
        actions.sendKeys(testData.get("Is Delay Notification Enabled"));
        actions.sendKeys(Keys.ENTER).build().perform();
        getDapiShortCollConfig().sendKeys(testData.get("Dapi Short Collection Config"));
        getCheckInAccDistance().sendKeys(testData.get("Cee Check in Acceptable Distance (meter)"));
        getCheckInDeltaTime().sendKeys(testData.get("Cee Check in Delta Time Minutes"));
        getCommBuffTimeMin().sendKeys(testData.get("Comm Buffer Time Minutes"));
        getEtaBuffrTimeMin().sendKeys(testData.get("ETA Buffer Time Minutes"));
        getEtaMinMaxConfig().sendKeys(testData.get("ETA Min Max Config"));
        getDnSilWindow().sendKeys(testData.get("DN Silent Window"));
        getdapiCallEretail().click();
        actions.sendKeys(testData.get("Dapi Call Eretail for Crate Info"));
        actions.sendKeys(Keys.ENTER).build().perform();
        getDapiGrpLinkdOrdr().click();
        actions.sendKeys(testData.get("Dapi Group Linked Orders"));
        actions.sendKeys(Keys.ENTER).build().perform();
        getDelayDelTmeMin().sendKeys(testData.get("Delay Delta Time Minutes"));
        getgraceDelTimeMin().sendKeys(testData.get("Grace Delivery Time Minutes"));
        getSkinActions().sendKeys(testData.get("Skip Actions"));
        getSkipCallBuffer().sendKeys(testData.get("Skip Call Buffer"));
        getWarningperiod().sendKeys(testData.get("Warning Period"));
        getEnableEq().click();
        actions.sendKeys(testData.get("Enable EQ"));
        actions.sendKeys(Keys.ENTER).build().perform();
        getEnableCeepayloadChosn().click();
        actions.sendKeys(testData.get("Enable Sync CEE Device Payload"));
        actions.sendKeys(Keys.ENTER).build().perform();
        getDelTypeLable().sendKeys(testData.get("Delivery Label Type"));
        getdapiDateSelEnableChosen().click();
        actions.sendKeys(testData.get("Enable Date Selection In Dapi"));
        actions.sendKeys(Keys.ENTER).build().perform();
        getAddnalConfigg().sendKeys(testData.get("Additional Config"));
        Thread.sleep(5000);
        submitButton.click();
        String message = messageValidation().getText();
        boolean messageValue = message.contains("created successfully") || message.contains("already exists");
        Assert.assertEquals(messageValue, true, "Message is Displayed");

    }

    public void hubopsECGroup(Map<String, String> testData) {
        addButton.click();
        getECGroupName().click();
        getECGroupName().sendKeys(testData.get("Group Name"));
        getDescription().sendKeys(testData.get("Description"));
        submitButton.click();
        String message = messageValidation().getText();
        boolean messageValue = message.contains("created successfully") || message.contains("already exists");
        Assert.assertEquals(messageValue, true, "Message is Displayed");

    }
    public void globalSADMConfig(Map<String, String> testData) throws InterruptedException {
        addButton.click();
        getSAID().click();
        Actions actions = new Actions(driver);
        actions.sendKeys(testData.get("Serviceability Area"));
        actions.sendKeys(Keys.ENTER).build().perform();
        getDeliveryMode1().click();
        actions.sendKeys(testData.get("Delivery Mode"));
        actions.sendKeys(Keys.ENTER).build().perform();
        getVehicleType1().click();
        actions.sendKeys(testData.get("Vehicle Type"));
        actions.sendKeys(Keys.ENTER).build().perform();
        getMailingList().sendKeys(testData.get("Mailing List"));
        getMaxWaitTimeforRouting().sendKeys(testData.get("Max Wait Time for Routing"));
        getIntervalBetnRouting().sendKeys(testData.get("Interval Between Routing"));
        getMaxFracnOfOrderstoMergeinManual().sendKeys(testData.get("Max Fraction of Orders to Merge in Manual"));
        Thread.sleep(2000);
        getPrimaryContactNo().sendKeys(testData.get("Primary Contact No"));
        getSecondaryContactNo().sendKeys(testData.get("Secondary Contact No"));
        getTertiaryContactNo().sendKeys(testData.get("Tertiary Contact No"));
        getAlternateContactNo().sendKeys(testData.get("Alternate Contact No"));
        Thread.sleep(2000);
        getDispatchTimeOffsetfortheSlots1().sendKeys(testData.get("Dispatch Time Offset for the Slots"));
        getDispatchTimeOffsetfortheNowSlots1().sendKeys(testData.get("Dispatch Time Offset for the Now Slots"));
        getLatestRouteAssignmentEligibility().sendKeys(testData.get("Latest Route Assignment Eligibility"));
        getNumofConsWinsAssign().sendKeys(testData.get("Number of Consecutive Windows Assignment"));
        getRoutingConfig().click();
        actions.sendKeys(testData.get("Routing Configuration"));
        actions.sendKeys(Keys.ENTER).build().perform();
        getMapProvider().click();
        actions.sendKeys(testData.get("Map Provider"));
        actions.sendKeys(Keys.ENTER).build().perform();
        Thread.sleep(2000);
        getCSContact().sendKeys(testData.get("CS Contact"));
        getConferenceDID().sendKeys(testData.get("Conference DID"));
        getIsDappPilot().click();
        actions.sendKeys(testData.get("Is Dapp Pilot"));
        actions.sendKeys(Keys.ENTER).build().perform();
        getDappPilotCEEList().sendKeys(testData.get("Dapp Pilot CEE List"));
        getDeliverAppVersion().click();
        actions.sendKeys(testData.get("Delivery App Version"));
        actions.sendKeys(Keys.ENTER).build().perform();
        getIsSkipbyOCEnabled().click();
        actions.sendKeys(testData.get("Is Skip by OC Enabled"));
        actions.sendKeys(Keys.ENTER).build().perform();
        getIsEarlyDeliveryAllowed().click();
        actions.sendKeys(testData.get("Is Early Delivery Allowed"));
        actions.sendKeys(Keys.ENTER).build().perform();
        getIsDelayNotifEnabled().click();
        actions.sendKeys(testData.get("Is Delay Notification Enabled"));
        actions.sendKeys(Keys.ENTER).build().perform();
        getEnableDelPhotos().click();
        actions.sendKeys(testData.get("Enable Delivery Photos"));
        actions.sendKeys(Keys.ENTER).build().perform();
        getEnableCusttoCEECalls().click();
        actions.sendKeys(testData.get("Enable Customer to CEE Calls"));
        actions.sendKeys(Keys.ENTER).build().perform();
        getAllowAssignfromUI().click();
        actions.sendKeys(testData.get("Allow Assignment from UI"));
        actions.sendKeys(Keys.ENTER).build().perform();
        getAllowQRScanCheckIn().click();
        actions.sendKeys(testData.get("Allow QR Scan CheckIn"));
        actions.sendKeys(Keys.ENTER).build().perform();
        getLocSyncInterval().sendKeys(testData.get("Location Sync Interval"));
        getTripLocSyncInterval().sendKeys(testData.get("Trip Location Sync Interval"));
        getBinScanDelta().sendKeys(testData.get("Bin Scan Delta"));
        getCommBufferTimeMin().sendKeys(testData.get("Comm Buffer Time Minutes"));
        getETABufferTimeMin().sendKeys(testData.get("ETA Buffer Time Minutes"));
        getETAMinMaxConfig().sendKeys(testData.get("ETA Min Max Config"));
        getDelayDeltaTimeMin().sendKeys(testData.get("Delay Delta Time Minutes"));
        getDNSilentWindow().sendKeys(testData.get("DN Silent Window"));
        getRecEmailIdsforDelExcReport().sendKeys(testData.get("Recipient Email ids for Delivery Exception Report"));
        getModeOfRouting().click();
        actions.sendKeys(testData.get("Mode of Routing"));
        actions.sendKeys(Keys.ENTER).build().perform();
        getIsDynamicVehicleConfEnabled().click();
        actions.sendKeys(testData.get("Is Dynamic Vehicle Conf Enabled"));
        actions.sendKeys(Keys.ENTER).build().perform();
        getDynamicVehicleConf1().sendKeys(testData.get("Dynamic Vehicle Config"));
        getSendFullCompartDetailsToRouteProvider().click();
        actions.sendKeys(testData.get("Send Full Compartment Details to Route Provider"));
        actions.sendKeys(Keys.ENTER).build().perform();
        getMinDrivingTime().sendKeys(testData.get("Min Driving Time"));
        getAddnalConfigg().sendKeys(testData.get("Additional Config"));
        Thread.sleep(1000);
        submitButton.click();
        String message = messageValidation().getText();
        boolean messageValue = message.contains("created successfully") || message.contains("already exists");
        Assert.assertEquals(messageValue, true, "Message is Displayed");

    }

    public void globalSADMProvderConfig(Map<String, String> testData) throws InterruptedException {
        addButton.click();
        getSAID().click();
        Actions actions = new Actions(driver);
        actions.sendKeys(testData.get("Serviceability Area"));
        actions.sendKeys(Keys.ENTER).build().perform();
        getDeliveryMode1().click();
        actions.sendKeys(testData.get("Delivery Mode"));
        actions.sendKeys(Keys.ENTER).build().perform();
        getRoutingProvider().click();
        actions.sendKeys(testData.get("Routing Provider"));
        actions.sendKeys(Keys.ENTER).build().perform();
        getPriority().sendKeys(testData.get("Priority"));
        Thread.sleep(2000);
        getProfile().click();
        actions.sendKeys(testData.get("Profile"));
        actions.sendKeys(Keys.ENTER).build().perform();
        getVehicleUsageFactor().click();
        actions.sendKeys(testData.get("Vehicle Usage Factor"));
        actions.sendKeys(Keys.ENTER).build().perform();
        getAdditionalConfig1().sendKeys(testData.get("Additional Config"));
        Thread.sleep(2000);
        submitButton.click();
        String message = messageValidation().getText();
        boolean messageValue = message.contains("created successfully") || message.contains("already exists");
        Assert.assertEquals(messageValue, true, "Message is Displayed");

    }
    public void deliveryAppVersion(Map<String, String> testData) throws InterruptedException {
        addButton.click();
        getAppVersion().sendKeys(testData.get("App Version"));
        getApkFile().sendKeys(System.getProperty("user.dir")+"//src//test//resources//apks//BB-Delivery-6.0.0-debug.apk");
        getCheckSum().sendKeys(testData.get("Check Sum"));
        getAdditionalData().sendKeys(testData.get("Additional Data"));
        submitButton.click();
        String message = messageValidation().getText();
        boolean messageValue = message.contains("created successfully") || message.contains("already exists");
        Assert.assertEquals(messageValue, true, "Message is Displayed");

    }
}
