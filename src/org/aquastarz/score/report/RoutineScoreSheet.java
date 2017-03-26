package org.aquastarz.score.report;

public class RoutineScoreSheet {
	private String ageGroup;
	private String theme;
	private Integer routineNum;
	private String judgeNum;
	private Integer execution;
	private Integer synchronization;
	private Integer difficulty;
	private Integer choreography;
	private Integer music;
	private Integer presentation;
	private String event;
	private String meetName;
	private String meetDate;
	private String participants;
	private String team;

	public RoutineScoreSheet() {

	}

	public RoutineScoreSheet(RoutineScoreSheet rss, String judge) {
		this.ageGroup = rss.getAgeGroup();
		this.theme = rss.getTheme();
		this.routineNum = rss.getRoutineNum();
		this.judgeNum = judge;
		this.execution = rss.getExecution();
		this.synchronization = rss.getSynchronization();
		this.difficulty = rss.getDifficulty();
		this.choreography = rss.getChoreography();
		this.music = rss.getMusic();
		this.presentation = rss.getPresentation();
		this.event = rss.getEvent();
		this.meetName = rss.meetName;
		this.meetDate = rss.meetDate;
		this.participants = rss.participants;
		this.team = rss.team;
	}

	public String getAgeGroup() {
		return ageGroup;
	}

	public void setAgeGroup(String ageGroup) {
		this.ageGroup = ageGroup;
	}

	public String getTheme() {
		return theme;
	}

	public void setTheme(String theme) {
		this.theme = theme;
	}

	public Integer getRoutineNum() {
		return routineNum;
	}

	public void setRoutineNum(Integer routineNum) {
		this.routineNum = routineNum;
	}

	public String getJudgeNum() {
		return judgeNum;
	}

	public void setJudgeNum(String judgeNum) {
		this.judgeNum = judgeNum;
	}

	public Integer getExecution() {
		return execution;
	}

	public void setExecution(Integer execution) {
		this.execution = execution;
	}

	public Integer getSynchronization() {
		return synchronization;
	}

	public void setSynchronization(Integer synchronization) {
		this.synchronization = synchronization;
	}

	public Integer getDifficulty() {
		return difficulty;
	}

	public void setDifficulty(Integer difficulty) {
		this.difficulty = difficulty;
	}

	public Integer getChoreography() {
		return choreography;
	}

	public void setChoreography(Integer choreography) {
		this.choreography = choreography;
	}

	public Integer getMusic() {
		return music;
	}

	public void setMusic(Integer music) {
		this.music = music;
	}

	public Integer getPresentation() {
		return presentation;
	}

	public void setPresentation(Integer presentation) {
		this.presentation = presentation;
	}

	public String getEvent() {
		return event;
	}

	public void setEvent(String event) {
		this.event = event;
	}

	public String getMeetName() {
		return meetName;
	}

	public void setMeetName(String meetName) {
		this.meetName = meetName;
	}

	public String getMeetDate() {
		return meetDate;
	}

	public void setMeetDate(String meetDate) {
		this.meetDate = meetDate;
	}

	public String getParticipants() {
		return participants;
	}

	public void setParticipants(String participants) {
		this.participants = participants;
	}

	public String getTeam() {
		return team;
	}

	public void setTeam(String team) {
		this.team = team;
	}

}
