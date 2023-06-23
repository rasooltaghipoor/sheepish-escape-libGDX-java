package com.baranagames.sheepishescape.android;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.baranagames.sheepishescape.ActionResolver;
import com.baranagames.sheepishescape.MyFirstGame;
import com.baranagames.sheepishescape.android.util.basegame.GameHelper;
import com.baranagames.sheepishescape.android.util.basegame.GameHelper.GameHelperListener;
import com.baranagames.sheepishescape.android.util.billing.IabHelper;
import com.baranagames.sheepishescape.android.util.billing.IabResult;
import com.baranagames.sheepishescape.android.util.billing.Inventory;
import com.baranagames.sheepishescape.android.util.billing.Purchase;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.games.Games;
import com.google.android.gms.games.GamesStatusCodes;
import com.google.android.gms.games.leaderboard.LeaderboardVariant;
import com.google.android.gms.games.leaderboard.Leaderboards;

public class AndroidLauncher extends AndroidApplication implements GameHelperListener, ActionResolver{
	
	private GameHelper gameHelper;	
	// The helper object
	private IabHelper mHelper;
	//public static boolean IS_PREMIUM;
	
	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		//***************************** code related to billing service **************************************
		//*****************************************************************************************************
		 String base64EncodedPublicKey = "MIHNMA0GCSqGSIb3DQEBAQUAA4G7ADCBtwKBrwDUp3GZ5pcp3iatcQleEfEBKR2PWH/OlfYlMFZDVnc3hGiqAtDEuGOnp69lv8xC9tShy3a5YhW2GYGQ+5SH6U1BO/SQIs/GCKzHDvXFdRC/nGP8sVbN862YP7Ob/4VpmPaCLexIwAt7bENYTSg9vm1wlE+bsF7IIHzCfWNxWzX5zjYH8KI+BH27HYdQy/mRBNs9pgLeBTVP6rjp9SdUrGND6/W2BTCxMgBBtGTxRQkCAwEAAQ==";

		// compute your public key and store it in base64EncodedPublicKey
		mHelper = new IabHelper(this, base64EncodedPublicKey);
			    
		// enable debug logging (for a production application, you should set this to false).
		mHelper.enableDebugLogging(true);
			   
		MyFirstGame.IS_PREMIUM_TMP = false;
		
		mHelper.startSetup(new IabHelper.OnIabSetupFinishedListener() {
		    public void onIabSetupFinished(IabResult result) {
			    if (!result.isSuccess()) {
			    // Oh noes, there was a problem.
			        Log.d("IAB", "Problem setting up In-app Billing Dadash: " + result);
			        return;
			    }            
			    // Hooray, IAB is fully set up!
			    Log.d("IAB", "Billing Success Dadash: " + result);
			    mHelper.queryInventoryAsync(mGotInventoryListener);
			}
		});
		
		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
		initialize(new MyFirstGame(this), config);
		
		//************************************ code related to game service ***********************************
		//*****************************************************************************************************
			
		if (gameHelper == null) {
		    gameHelper = new GameHelper(this, GameHelper.CLIENT_GAMES);
		    gameHelper.enableDebugLog(true);
		}
		gameHelper.setup(this);
	}
	
	//*********************************** code related to billing service *****************************************
		//*****************************************************************************************************
		
		@Override
		public void onDestroy() {
		   super.onDestroy();
		   if (mHelper != null) mHelper.dispose();
		   mHelper = null;
		}
		
		@Override
		public void purchasePremium() {
		     mHelper.launchPurchaseFlow(this, SKU_PREMIUM, RC_REQUEST,
		    		 mPurchaseFinishedListener, "HANDLE_PAYLOADS");
		 }
		
		@Override
		public void processPurchases(){
			mHelper.queryInventoryAsync(mGotInventoryListener);
		}
		
		// Callback for when a purchase is finished
	    IabHelper.OnIabPurchaseFinishedListener mPurchaseFinishedListener = new IabHelper.OnIabPurchaseFinishedListener() {
	        public void onIabPurchaseFinished(IabResult result, Purchase purchase) {
	            if ( purchase == null) return;
	            Log.d("IAB", "Purchase finished: " + result + ", purchase: " + purchase);
	            // if we were disposed of in the meantime, quit.
	            if (mHelper == null) return;
	            if (result.isFailure()) {              
	                return;
	            }//           
	            Log.d("IAB", "Purchase successful.");
	            if (purchase.getSku().equals(SKU_PREMIUM)) {
	                // bought the premium upgrade!
	                Log.d("IAB", "Purchase is premium upgrade. Congratulating user.");
	                // Do what you want here maybe call your game to do some update                //
	            	// Maybe set a flag to indicate that ads shouldn't show anymore
	                MyFirstGame.IS_PREMIUM_TMP = true;
	            }
	        }
	    };
	    
	 // Listener that's called when we finish querying the items and subscriptions we own
	    IabHelper.QueryInventoryFinishedListener mGotInventoryListener = new IabHelper.QueryInventoryFinishedListener() {
	        public void onQueryInventoryFinished(IabResult result, Inventory inventory) {
	            Log.d("IAB", "Query inventory finished.");

	            // Have we been disposed of in the meantime? If so, quit.
	            if (mHelper == null) return;

	            // Is it a failure?
	            if (result.isFailure()) {
	                // handle failure here
	            	Log.d("Test","Failed to query inventory: " + result);
	                return;
	            }

	            // Do we have the premium upgrade?
	          /*  Purchase removeAdPurchase = inventory.getPurchase(SKU_PREMIUM);
	            MyFirstGame.IS_PREMIUM = (removeAdPurchase != null);*/
	            MyFirstGame.IS_PREMIUM_TMP = inventory.hasPurchase(SKU_PREMIUM);
	           // GetMoney.isPremium = inventory.hasPurchase(SKU_REMOVE_ADS);
	        }
	    };

		//****************************************** code related to game service ********************************************************
		//*****************************************************************************************************
		
		@Override
		public void onStart(){
			super.onStart();
			gameHelper.onStart(this);
		}

		@Override
		public void onStop(){
			super.onStop();
			gameHelper.onStop();
		}

		@Override
		public void onActivityResult(int request, int response, Intent data) {
			super.onActivityResult(request, response, data);
			//************** for game service
			
			gameHelper.onActivityResult(request, response, data);
			//*************  for billing
			if (mHelper != null) {
		        // Pass on the activity result to the helper for handling
		        /*if (mHelper.handleActivityResult(request, response, data)) {
		            Log.d("IAB", "onActivityResult handled by IABUtil.");
		        }*/
		        if (!mHelper.handleActivityResult(request, response, data)) {
		            super.onActivityResult(request, response, data);
		        } else {
		            Log.d("Test2", "onActivityResult handled by IABUtil.");
		        }
		    }      
		       
		}
				
		@Override
		public boolean getSignedInGPGS() {
			return gameHelper.isSignedIn();
		}

		@Override
		public void loginGPGS() {
			try {
					runOnUiThread(new Runnable(){
						public void run() {
							gameHelper.beginUserInitiatedSignIn();
						}
					});
			} catch (final Exception ex) {
			}
		}

		@Override
		public void submitScoreGPGS(int score) {
			Games.Leaderboards.submitScore(gameHelper.getApiClient(), "CgkI0Ka5p9UBEAIQCA", score);
		}
				
		@Override
		public void unlockAchievementGPGS(String achievementId) {
			Games.Achievements.unlock(gameHelper.getApiClient(), achievementId);
		}
				
		@Override
		public void getLeaderboardGPGS() {
			if (gameHelper.isSignedIn()) {
			    startActivityForResult(Games.Leaderboards.getLeaderboardIntent(gameHelper.getApiClient(), "CgkI0Ka5p9UBEAIQCA"), 100);
			}
			else if (!gameHelper.isConnecting()) {
				loginGPGS();
			}
		}

		@Override
		public void getAchievementsGPGS() {
		    if (gameHelper.isSignedIn()) {
			    startActivityForResult(Games.Achievements.getAchievementsIntent(gameHelper.getApiClient()), 101);
		    }
		    else if (!gameHelper.isConnecting()) {
			    loginGPGS();
		    }
		}
		
		/* public void getLeaderboradScore() {
		    Games.Leaderboards.loadCurrentPlayerLeaderboardScore(
		         gameHelper.getApiClient(), "CgkI0Ka5p9UBEAIQCA",  LeaderboardVariant.TIME_SPAN_ALL_TIME, 
		         LeaderboardVariant.COLLECTION_PUBLIC).setResultCallback(new ResultCallback<Leaderboards.LoadPlayerScoreResult>() {
		            @Override
		            public void onResult(Leaderboards.LoadPlayerScoreResult loadPlayerScoreResult) {
		                if (loadPlayerScoreResult != null) {
		                    if (GamesStatusCodes.STATUS_OK == loadPlayerScoreResult.getStatus().getStatusCode()) {
		                        long score = 0;
		                        if (loadPlayerScoreResult.getScore() != null) {
		                            score = loadPlayerScoreResult.getScore().getRawScore();
		                        }
		                        Games.Leaderboards.submitScore(gameHelper.getApiClient(), "CgkI0Ka5p9UBEAIQCA", ++score);
		                    }
		                }
		            }

		        });
		}*/
			
		@Override
		public void onSignInFailed() {		
		}

		@Override
		public void onSignInSucceeded() {
		}
}
