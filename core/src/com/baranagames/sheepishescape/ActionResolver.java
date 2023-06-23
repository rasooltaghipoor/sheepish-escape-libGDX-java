package com.baranagames.sheepishescape;

public interface ActionResolver {
	 
	//************* billing service vars and functions ***********
	public String SKU_PREMIUM = "activate_premium4";
    // (arbitrary) request code for the purchase flow     
    static final int RC_REQUEST = 10001;
       
    public void purchasePremium();
    public void processPurchases();
    
    // ************** game service functions ***************
	public boolean getSignedInGPGS();
	public void loginGPGS();
	public void submitScoreGPGS(int score);
	public void unlockAchievementGPGS(String achievementId);
	public void getLeaderboardGPGS();
	public void getAchievementsGPGS();
	//public void getLeaderboradScore();
}
