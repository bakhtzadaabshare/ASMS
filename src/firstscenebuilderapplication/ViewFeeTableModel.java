
package firstscenebuilderapplication;

/**
 *
 * @author Bakht Zada
 */
public class ViewFeeTableModel {
   String class_name, promotionFee, MonthlyFee;
    public ViewFeeTableModel(String class_name, String promotionFee, String MonthlyFee) {
        this.class_name = class_name;
        this.promotionFee = promotionFee;
        this.MonthlyFee = MonthlyFee;
    }

    public String getClass_name() {
        return class_name;
    }

    public String getPromotionFee() {
        return promotionFee;
    }

    public String getMonthlyFee() {
        return MonthlyFee;
    }

    public void setClass_name(String class_name) {
        this.class_name = class_name;
    }

    public void setPromotionFee(String promotionFee) {
        this.promotionFee = promotionFee;
    }

    public void setMonthlyFee(String MonthlyFee) {
        this.MonthlyFee = MonthlyFee;
    }
   
}
