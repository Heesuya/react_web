package kr.co.iei.util;

import org.springframework.stereotype.Component;

@Component
public class PageUtil {
	
	public PageInfo getPageInfo(int reqPage, int numPerpage, int pageNaviSize, int totalCount) {
		int end = reqPage * numPerpage;
		int start = end - numPerpage + 1;
		//int totalPage=(totalCount%numPerpage != 0? totalCount/numPerpage+1 : totalCount/numPerpage+1);
		int totalPage = (int)Math.ceil(totalCount/(double)numPerpage);
		int pageNo = ((reqPage-1)/pageNaviSize)*pageNaviSize +1;
		PageInfo pi = new PageInfo(start, end, pageNo, pageNaviSize, totalPage);
		return pi;
	}
}
