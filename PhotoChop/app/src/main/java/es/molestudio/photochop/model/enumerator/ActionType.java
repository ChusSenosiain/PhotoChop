package es.molestudio.photochop.model.enumerator;

/**
 * Created by Chus on 04/01/15.
 */
public enum ActionType {
    CREATE(0, "CREATE"),
    UPDATE(1, "UPDATE"),
    DELETE(2, "DELETE"),
    NONE(3, "NONE");

    private int mActionType;
    private String mActionTypeDesc;


    ActionType(int actionType, String actionTypeDesc) {
        mActionType = actionType;
        mActionTypeDesc = actionTypeDesc;
    }
}
