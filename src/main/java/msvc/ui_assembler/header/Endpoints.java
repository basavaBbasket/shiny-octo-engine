package msvc.ui_assembler.header;

import com.bigbasket.automation.Config;

public interface Endpoints {

    String UI_ASSEMBLER_VERSION="v1";
    String SEND_DOOR_INFO="/ui-svc/"+UI_ASSEMBLER_VERSION+"/header?send_door_info=%s";
    String SEND_DOOR_INFO_CONFIG="/config_svc/internal/"+UI_ASSEMBLER_VERSION+"/config/?";
}
