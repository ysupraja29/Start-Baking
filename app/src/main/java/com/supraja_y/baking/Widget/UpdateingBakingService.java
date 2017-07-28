
package com.supraja_y.baking.Widget;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;

import java.util.ArrayList;

/**
 * Created by suprajayerranagu on 24/07/17.
 */
public class UpdateingBakingService extends IntentService {

    public static String FROM_ACTIVITY_INGREDIENTS_LIST ="FROM_ACTIVITY_INGREDIENTS_LIST";

    public UpdateingBakingService() {
        super("UpdateingBakingService");
    }

    public static void startBakingService(Context context,ArrayList<String> fromActivityIngredientsList) {
        Intent intent = new Intent(context, UpdateingBakingService.class);
        intent.putExtra(FROM_ACTIVITY_INGREDIENTS_LIST,fromActivityIngredientsList);
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            ArrayList<String> fromActivityIngredientsList = intent.getExtras().getStringArrayList(FROM_ACTIVITY_INGREDIENTS_LIST);
            handleActionUpdateBakingWidgets(fromActivityIngredientsList);

        }
    }

    private void handleActionUpdateBakingWidgets(ArrayList<String> fromActivityIngredientsList) {
            Intent intent = new Intent("android.appwidget.action.APPWIDGET_UPDATE2");
            intent.setAction("android.appwidget.action.APPWIDGET_UPDATE2");
            intent.putExtra(FROM_ACTIVITY_INGREDIENTS_LIST,fromActivityIngredientsList);
            sendBroadcast(intent);
       }

    }
