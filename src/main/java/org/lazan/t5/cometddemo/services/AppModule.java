package org.lazan.t5.cometddemo.services;

import java.util.Random;

import org.apache.tapestry5.SymbolConstants;
import org.apache.tapestry5.ioc.MappedConfiguration;
import org.apache.tapestry5.ioc.OrderedConfiguration;
import org.apache.tapestry5.ioc.ServiceBinder;
import org.apache.tapestry5.ioc.annotations.Startup;
import org.lazan.t5.cometd.ClientContext;
import org.lazan.t5.cometd.services.Authorizer;
import org.lazan.t5.cometd.services.PushManager;
import org.lazan.t5.cometd.services.SubscriptionListener;
import org.lazan.t5.cometddemo.stocks.StockPrice;

/**
 * This module is automatically included as part of the Tapestry IoC Registry, it's a good place to
 * configure and extend Tapestry, or to place your own service definitions.
 */
public class AppModule
{
    public static void bind(ServiceBinder binder)
    {
        // binder.bind(MyServiceInterface.class, MyServiceImpl.class);

        // Make bind() calls on the binder object to define most IoC services.
        // Use service builder methods (example below) when the implementation
        // is provided inline, or requires more initialization than simply
        // invoking the constructor.
    }

    public static void contributeFactoryDefaults(
            MappedConfiguration<String, Object> configuration)
    {
        // The application version number is incorprated into URLs for some
        // assets. Web browsers will cache assets because of the far future expires
        // header. If existing assets are changed, the version number should also
        // change, to force the browser to download new versions. This overrides Tapesty's default
        // (a random hexadecimal number), but may be further overriden by DevelopmentModule or
        // QaModule.
        configuration.override(SymbolConstants.APPLICATION_VERSION, "1.0-SNAPSHOT");
    }

    public static void contributeApplicationDefaults(
            MappedConfiguration<String, Object> configuration)
    {
        // Contributions to ApplicationDefaults will override any contributions to
        // FactoryDefaults (with the same key). Here we're restricting the supported
        // locales to just "en" (English). As you add localised message catalogs and other assets,
        // you can extend this list of locales (it's a comma separated series of locale names;
        // the first locale name is the default when there's no reasonable match).
        configuration.add(SymbolConstants.SUPPORTED_LOCALES, "en");
    }
    
    public static void contributeAuthorizers(OrderedConfiguration<Authorizer> config) {
    	Authorizer auth = new Authorizer() {
			public boolean isAuthorized(ClientContext clientContext) {
				System.err.println(String.format("isAuthorized(%s)", clientContext.getTopic()));
				return true;
			}
			
			public String getTopicPattern() {
				return "**";
			}
		};
		config.add("print", auth);
    	
    }
    
    public static void contributeSubscriptionListeners(OrderedConfiguration<SubscriptionListener> config) {
    	SubscriptionListener listener = new SubscriptionListener() {
    		public String getTopicPattern() {
    			return "**";
    		}
    		public void onSubscribe(ClientContext context) {
    			System.err.println(String.format("onSubscribe(%s)", context.getTopic()));
    		}
    		public void onUnsubscribe(ClientContext context) {
    			System.err.println(String.format("onUnsubscribe(%s)", context.getTopic()));
    		}
    	};
    	config.add("print", listener);
    }
    
    @Startup
    public static void startStockTicker(final PushManager pushManager) {
    	final String[] tickers = { "GOOG", "YAHOO", "IBM", "SONY" };
    	final Random random = new Random();
    	Runnable runnable = new Runnable() {
    		public void run() {
    			while (true) {
	    			String ticker = tickers[random.nextInt(tickers.length)];
	    			double price = random.nextInt(10000) / 100D;
	    			
	    			String topic = "stocks/" + ticker;
	    			pushManager.broadcast(topic, new StockPrice(ticker, price));
	        		try {
	    				Thread.sleep(1000);
	    			} catch (InterruptedException e) {}
    			}
    		}
    	};
    	new Thread(runnable).start();
    }
}
