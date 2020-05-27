package snakeandladder.taskcallable;

@FunctionalInterface
public interface TaskCallable {
    void task(TaskCallArgument arg);

    class TaskCallArgument {
        private int frameCount = 0;

        public void addFrameCount() {
            frameCount++;
        }

        public int getFrameCount() {
            return frameCount;
        }
    }
}
