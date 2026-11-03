
package core;

public interface Model {
    void attach(View view);
    void detach(View view);
    void notifyViews();
}