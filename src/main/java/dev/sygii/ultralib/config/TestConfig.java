package dev.sygii.ultralib.config;

public class TestConfig extends UltraConfig<TestConfig.Config> {

    public TestConfig() {
        super("test", Config.class, new Config());
    }

    public static class Config {
        public boolean boo = false;
    }
}
