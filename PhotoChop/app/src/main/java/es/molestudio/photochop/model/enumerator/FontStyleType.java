package es.molestudio.photochop.model.enumerator;

/**
 * Created by Chus on 08/01/15.
 */
public enum FontStyleType {
    REGULAR(0, "Regular", "Roboto-Regular.ttf"),
    BOLD(1, "Bold", "Roboto-Bold.ttf"),
    BLACK(2, "Black", "Roboto-Black.ttf"),
    LIGHT(3, "Light", "Roboto-Light.ttf"),
    MEDIUM(4, "Medium", "Roboto-Medium.ttf"),
    THIN(5, "Thin", "Roboto-Thin.ttf");


    private int mFontStyleTypeId;
    private String mFontStyleTypeDesc;
    private String mFontStyleTypeAsset;


    FontStyleType(int fontStyleTypeId, String fontStyleTypeDesc, String fontStyleTypeAsset) {
        mFontStyleTypeId = fontStyleTypeId;
        mFontStyleTypeDesc = fontStyleTypeDesc;
        mFontStyleTypeAsset = fontStyleTypeAsset;
    }



    public static FontStyleType valueOf(Integer fontStyleTypeId) {

        switch (fontStyleTypeId) {
            case 0:
                return FontStyleType.REGULAR;
            case 1:
                return FontStyleType.BOLD;
            case 2:
                return FontStyleType.BLACK;
            case 3:
                return FontStyleType.LIGHT;
            case 4:
                return FontStyleType.MEDIUM;
            case 5:
                return FontStyleType.THIN;
            default:
                return FontStyleType.REGULAR;
        }
    }


    public String getFontStyleTypeAsset() {
        return mFontStyleTypeAsset;
    }

    public int getFontStyleTypeId() {
        return mFontStyleTypeId;
    }
}
