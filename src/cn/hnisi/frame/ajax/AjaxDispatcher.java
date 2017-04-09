package cn.hnisi.frame.ajax;

public interface AjaxDispatcher {

	/**
	 * ajax调用统一入口
	 * @param inParam
	 * @return
	 */
	public AjaxOutParam ajax(AjaxInParam inParam);
}
