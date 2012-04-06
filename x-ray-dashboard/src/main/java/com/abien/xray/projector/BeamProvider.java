package com.abien.xray.projector;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.concurrent.Worker;

/**
 *
 * @author Adam Bien <blog.adam-bien.com>
 */
public class BeamProvider extends Service<String> {

    private final String liveDataURL;
    private BeamListener listener;

    public BeamProvider(String liveDataURL) {
        this.liveDataURL = liveDataURL;
        this.startFetching();
    }

    @Override
    protected Task<String> createTask() {
        return new Task<String>() {
           
            @Override
            protected String call() throws Exception {
                BeamFetcher fetcher = new BeamFetcher(liveDataURL);
                System.out.println("Fetching: " + liveDataURL);
                return fetcher.getSnapshot();
            }
        };
    }
    

    final void startFetching() {
        this.start();
        this.valueProperty().addListener(
                new ChangeListener<String>() {

                    public void changed(ObservableValue<? extends String> observable, String old, String newValue) {
                        if (newValue != null) {
                            onBeamArrival(newValue);
                        }
                    }


                });
        registerRestarting();
    }  
    
    void registerRestarting() {
        this.stateProperty().addListener(new ChangeListener<Worker.State>() {
            public void changed(ObservableValue<? extends Worker.State> observable, Worker.State oldState, Worker.State newState) {
                if (newState.equals(Worker.State.SUCCEEDED) || newState.equals(Worker.State.FAILED)) {
                    reset();
                    start();
                }
            }
        });
    }
    
    void onBeamArrival(String newValue) {
        if(this.listener != null)
           this.listener.onBeamArrival(newValue);
    }

    public void setBeamListener(BeamListener listener) {
        this.listener = listener;
    }
}