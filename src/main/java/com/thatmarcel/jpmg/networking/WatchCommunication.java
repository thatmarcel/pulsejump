package com.thatmarcel.jpmg.networking;

import com.almasb.fxgl.dsl.FXGL;
import com.pubnub.api.PNConfiguration;
import com.pubnub.api.PubNub;
import com.pubnub.api.callbacks.SubscribeCallback;
import com.pubnub.api.models.consumer.PNStatus;
import com.pubnub.api.models.consumer.objects_api.channel.PNChannelMetadataResult;
import com.pubnub.api.models.consumer.objects_api.membership.PNMembershipResult;
import com.pubnub.api.models.consumer.objects_api.uuid.PNUUIDMetadataResult;
import com.pubnub.api.models.consumer.pubsub.PNMessageResult;
import com.pubnub.api.models.consumer.pubsub.PNPresenceEventResult;
import com.pubnub.api.models.consumer.pubsub.PNSignalResult;
import com.pubnub.api.models.consumer.pubsub.files.PNFileEventResult;
import com.pubnub.api.models.consumer.pubsub.message_actions.PNMessageActionResult;
import com.thatmarcel.jpmg.helpers.pulse.PulseAction;
import com.thatmarcel.jpmg.helpers.ui.UIManager;
import javafx.util.Duration;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;

public class WatchCommunication {
    public static void start() {
        PNConfiguration pnConfiguration = new PNConfiguration();
        pnConfiguration.setSubscribeKey("sub-c-45eb9030-3a50-11eb-ab29-9acd6868450b");
        pnConfiguration.setPublishKey("pub-c-1b9d9b9d-5b17-4df5-aebb-0cbc9dac1149");

        PubNub pubnub = new PubNub(pnConfiguration);

        pubnub.addListener(new SubscribeCallback() {

            @Override
            public void message(@NotNull PubNub pubnub, @NotNull PNMessageResult pnMessageResult) {
                String message = pnMessageResult.getMessage().toString();
                int bpm = Integer.parseInt(message.replaceAll("\"", ""));
                FXGL.getGameTimer().runOnceAfter(() -> UIManager.activeInstance.updateBPM(bpm), Duration.millis(10));
                PulseAction.lastBPM = bpm;
            }

            @Override public void status(@NotNull PubNub pubnub, @NotNull PNStatus pnStatus) { }
            @Override public void presence(@NotNull PubNub pubnub, @NotNull PNPresenceEventResult pnPresenceEventResult) { }
            @Override public void signal(@NotNull PubNub pubnub, @NotNull PNSignalResult pnSignalResult) { }
            @Override public void uuid(@NotNull PubNub pubnub, @NotNull PNUUIDMetadataResult pnUUIDMetadataResult) { }
            @Override public void channel(@NotNull PubNub pubnub, @NotNull PNChannelMetadataResult pnChannelMetadataResult) { }
            @Override public void membership(@NotNull PubNub pubnub, @NotNull PNMembershipResult pnMembershipResult) { }
            @Override public void messageAction(@NotNull PubNub pubnub, @NotNull PNMessageActionResult pnMessageActionResult) { }
            @Override public void file(@NotNull PubNub pubnub, @NotNull PNFileEventResult pnFileEventResult) { }
        });

        pubnub.subscribe()
                .channels(Collections.singletonList("heartrates"))
                .execute();
    }
}
