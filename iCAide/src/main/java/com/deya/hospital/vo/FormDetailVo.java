package com.deya.hospital.vo;

import com.deya.hospital.vo.dbdata.TaskVo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class FormDetailVo implements Serializable {
	private static final long serialVersionUID = 4380129839012L;
	private String title;
	private String author;
	private String hospitalName;
	private String department;
	private int scoreNum;
	private boolean canOpen=true;
	private int page;
	private String explains;
	int chooseItem=-1;
	boolean readMore;
	int lineCount;
	String method;
	int is_remark;
	
	List<Subrules> sub_rule=new ArrayList<Subrules>();
	/*
	 * 0 无状态，1 打开状态  ，2 关闭状态
	 */
	
	public String makeSubRuleText(){
		StringBuffer buffer = new StringBuffer();
		int i = 0;
		for(Subrules subRules : sub_rule ){
			if(subRules!=null){
				buffer.append(String.valueOf((char)('A'+i) ) );
				buffer.append(" ");
				buffer.append( subRules.name );
				buffer.append("\n");
			}
			++i;
		}
		
		return buffer.toString();
	}
	
	public int getIs_remark() {
		return is_remark;
	}

	public void setIs_remark(int is_remark) {
		this.is_remark = is_remark;
	}

	private int openState;
	
	
	
	public String getMethod() {
		return method;
	}

	public void setMethod(String method) {
		this.method = method;
	}

	public String getExplains() {
		return explains;
	}

	public void setExplains(String explains) {
		this.explains = explains;
	}

	private double score;
	public double getScore() {
		return score;
	}

	public void setScore(double score) {
		this.score = score;
	}

	
	public boolean isReadMore() {
		return readMore;
	}

	public void setReadMore(boolean readMore) {
		this.readMore = readMore;
	}

	public int getPage() {
		return page;
	}

	public void setPage(int page) {
		this.page = page;
	}

	
	
	public int getLineCount() {
		return lineCount;
	}

	public void setLineCount(int lineCount) {
		this.lineCount = lineCount;
	}

	/*
	 * 0 无状态，1 打开状态  ，2 关闭状态
	 */
	public int getOpenState() {
		return openState;
	}
	/*
	 * 0 无状态，1 打开状态  ，2 关闭状态
	 */
	public void setOpenState(int openState) {
		this.openState = openState;
	}

	public boolean isCanOpen() {
		return canOpen;
	}

	public void setCanOpen(boolean canOpen) {
		this.canOpen = canOpen;
	}

	private int nowScoreNum;

	public int getNowScoreNum() {
		return nowScoreNum;
	}

	public void setNowScoreNum(int nowScoreNum) {
		this.nowScoreNum = nowScoreNum;
	}

	public String getDepartment() {
		return department;
	}

	public void setDepartment(String department) {
		this.department = department;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public String getHospitalName() {
		return hospitalName;
	}

	public void setHospitalName(String hospitalName) {
		this.hospitalName = hospitalName;
	}

	public int getScoreNum() {
		return scoreNum;
	}

	public void setScoreNum(int scoreNum) {
		this.scoreNum = scoreNum;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public int getResult() {
		return result;
	}

	public void setResult(int result) {
		this.result = result;
	}

	public String getPid() {
		return pid;
	}

	public void setPid(String pid) {
		this.pid = pid;
	}

	public int getOrders() {
		return orders;
	}

	public void setOrders(int orders) {
		this.orders = orders;
	}

	public String getDescribes() {
		return describes;
	}

	public void setDescribes(String describes) {
		this.describes = describes;
	}

	public double getUnit_score() {
		return unit_score;
	}

	public void setUnit_score(double unit_score) {
		this.unit_score = unit_score;
	}

	public double getBase_score() {
		return base_score;
	}

	public void setBase_score(double base_score) {
		this.base_score = base_score;
	}

	public double getScores() {
		return scores;
	}

	public void setScores(double scores) {
		this.scores = scores;
	}


	public boolean isRemark() {
		return isRemark;
	}

	public void setRemark(boolean isRemark) {
		this.isRemark = isRemark;
	}





	public TaskVo getRemarkVo() {
		return remarkVo;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}




	public String getRemark() {
		return remark;
	}




	public void setRemarkVo(TaskVo remarkVo) {
		this.remarkVo = remarkVo;
	}




	public List<Subrules> getSub_rule() {
		
		return sub_rule;
	}

	public void setSub_rule(List<Subrules> sub_rule) {
		this.sub_rule.clear();
		this.sub_rule.addAll(sub_rule);
	}

	public int getChooseItem() {
		return chooseItem;
	}

	public void setChooseItem(int chooseItem) {
		this.chooseItem = chooseItem;
	}

	private String id;
	private int result;
	private String pid;
	private int orders;
	private String describes;
	private double unit_score;
	private double base_score;
	private double scores;
	private boolean isRemark;
	private TaskVo remarkVo;
	private String remark="";
	
	
	
}
