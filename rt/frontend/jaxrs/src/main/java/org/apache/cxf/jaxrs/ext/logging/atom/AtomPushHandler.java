begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one  * or more contributor license agreements. See the NOTICE file  * distributed with this work for additional information  * regarding copyright ownership. The ASF licenses this file  * to you under the Apache License, Version 2.0 (the  * "License"); you may not use this file except in compliance  * with the License. You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing,  * software distributed under the License is distributed on an  * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  * KIND, either express or implied. See the License for the  * specific language governing permissions and limitations  * under the License.  */
end_comment

begin_package
package|package
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|jaxrs
operator|.
name|ext
operator|.
name|logging
operator|.
name|atom
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|logging
operator|.
name|Handler
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|logging
operator|.
name|LogManager
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|jaxrs
operator|.
name|ext
operator|.
name|logging
operator|.
name|LogRecord
import|;
end_import

begin_comment
comment|/**  * Handler pushing log records in batches as Atom Feeds to registered client. Handler responsibility is to  * adapt to JUL framework while most of job is delegated to {@link AtomPushEngine}.  *<p>  * For simple configuration using properties file (one global root-level handler of this class) following  * properties prefixed with full class name can be used:  *<ul>  *<li><b>url</b> - URL where feeds will be pushed (mandatory parameter)</li>  *<li><b>converter</b> - name of class implementing {@link Converter} class. For classes from this package  * only class name can be given e.g. instead of  * "org.apache.cxf.jaxrs.ext.logging.atom.ContentSingleEntryConverter" one can specify  * "ContentSingleEntryConverter". If parameter is not set {@link ContentSingleEntryConverter} is used.</li>  *<li><b>deliverer</b> - name of class implementing {@link Deliverer} class. For classes from this package  * only class name can be given e.g. instead of "org.apache.cxf.jaxrs.ext.logging.atom.WebClientDeliverer" one  * can specify "WebClientDeliverer". If parameter is not set {@link WebClientDeliverer} is used.</li>  *<li><b>batchSize</b> - integer number specifying minimal number of published log records that trigger  * processing and pushing ATOM document. If parameter is not set, is not greater than zero or is not a number,  * batch size is set to 1.</li>  *</ul>  * Family of<tt>retry</tt> parameters below; availability of any of this parameters enables delivery retrying  * (e.g. for default non-reliable deliverers) with {@link RetryingDeliverer} that can be combined with  * provided non-reliable deliverers. Detailed explanation of these parameter, see {@link RetryingDeliverer}  * class description.  *<ul>  *<li><b>retry.pause</b> - pausing strategy of delivery retries, either<b>linear</b> or<b>exponential</b>  * value (mandatory parameter). If mispelled linear is used.</li>  *<li><b>retry.pause.time</b> - pause time (in seconds) between retries. If parameter is not set, pause is  * set to 30 seconds.</li>  *<li><b>retry.timeout</b> - maximum time (in seconds) retrying will be continued. If not set timeout is not  * set (infinite loop of retries).</li>  *</ul>  * Example:  *   *<pre>  * handlers = org.apache.cxf.jaxrs.ext.logging.atom.AtomPushHandler, java.util.logging.ConsoleHandler  * .level = INFO  * ...  * org.apache.cxf.jaxrs.ext.logging.atom.AtomPushHandler.url = http://localhost:9080  * org.apache.cxf.jaxrs.ext.logging.atom.AtomPushHandler.batchSize = 10  * org.apache.cxf.jaxrs.ext.logging.atom.AtomPushHandler.deliverer = WebClientDeliverer   * org.apache.cxf.jaxrs.ext.logging.atom.AtomPushHandler.converter = foo.bar.MyConverter  * org.apache.cxf.jaxrs.ext.logging.atom.AtomPushHandler.retry.pause = linear  * org.apache.cxf.jaxrs.ext.logging.atom.AtomPushHandler.retry.pause.time = 10  * org.apache.cxf.jaxrs.ext.logging.atom.AtomPushHandler.retry.timeout = 360  * ...  *</pre>  */
end_comment

begin_class
specifier|public
specifier|final
class|class
name|AtomPushHandler
extends|extends
name|Handler
block|{
specifier|private
name|AtomPushEngine
name|engine
decl_stmt|;
specifier|private
name|boolean
name|lazyConfig
decl_stmt|;
comment|/**      * Creates handler with configuration taken from properties file.      */
specifier|public
name|AtomPushHandler
parameter_list|()
block|{
comment|// deferred configuration: configure() called from here would use utilities that attempt to log
comment|// and create this handler instance in recursion; configure() will be called on first publish()
name|lazyConfig
operator|=
literal|true
expr_stmt|;
block|}
comment|/**      * Creates handler with custom parameters.      *       * @param batchSize batch size, see {@link AtomPushEngine#getBatchSize()}      * @param converter converter transforming logs into ATOM elements      * @param deliverer deliverer pushing ATOM elements to client      */
specifier|public
name|AtomPushHandler
parameter_list|(
name|int
name|batchSize
parameter_list|,
name|Converter
name|converter
parameter_list|,
name|Deliverer
name|deliverer
parameter_list|)
block|{
name|engine
operator|=
operator|new
name|AtomPushEngine
argument_list|()
expr_stmt|;
name|engine
operator|.
name|setBatchSize
argument_list|(
name|batchSize
argument_list|)
expr_stmt|;
name|engine
operator|.
name|setConverter
argument_list|(
name|converter
argument_list|)
expr_stmt|;
name|engine
operator|.
name|setDeliverer
argument_list|(
name|deliverer
argument_list|)
expr_stmt|;
block|}
comment|/**      * Creates handler using (package private).      *       * @param engine configured engine.      */
name|AtomPushHandler
parameter_list|(
name|AtomPushEngine
name|engine
parameter_list|)
block|{
name|this
operator|.
name|engine
operator|=
name|engine
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
specifier|synchronized
name|void
name|publish
parameter_list|(
name|java
operator|.
name|util
operator|.
name|logging
operator|.
name|LogRecord
name|record
parameter_list|)
block|{
if|if
condition|(
name|LoggingThread
operator|.
name|isSilent
argument_list|()
condition|)
block|{
return|return;
block|}
name|LoggingThread
operator|.
name|markSilent
argument_list|(
literal|true
argument_list|)
expr_stmt|;
try|try
block|{
if|if
condition|(
name|lazyConfig
condition|)
block|{
name|lazyConfig
operator|=
literal|false
expr_stmt|;
name|configure2
argument_list|()
expr_stmt|;
block|}
name|LogRecord
name|rec
init|=
name|LogRecord
operator|.
name|fromJUL
argument_list|(
name|record
argument_list|)
decl_stmt|;
name|engine
operator|.
name|publish
argument_list|(
name|rec
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
name|LoggingThread
operator|.
name|markSilent
argument_list|(
literal|false
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Override
specifier|public
specifier|synchronized
name|void
name|close
parameter_list|()
throws|throws
name|SecurityException
block|{
name|engine
operator|.
name|shutdown
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
specifier|synchronized
name|void
name|flush
parameter_list|()
block|{
comment|// no-op
block|}
comment|/**      * Configuration from properties. Aligned to JUL strategy - properties file is only for simple      * configuration: it allows configure one root handler with its parameters. What is even more dummy, JUL      * does not allow to iterate over configuration properties to make interpretation automated (e.g. using      * commons-beanutils)      */
comment|// private void configure() {
comment|// LogManager manager = LogManager.getLogManager();
comment|// String cname = getClass().getName();
comment|// String url = manager.getProperty(cname + ".url");
comment|// if (url == null) {
comment|// // cannot proceed
comment|// return;
comment|// }
comment|// String deliverer = manager.getProperty(cname + ".deliverer");
comment|// if (deliverer != null) {
comment|// engine.setDeliverer(createDeliverer(deliverer, url));
comment|// } else {
comment|// // default
comment|// engine.setDeliverer(new WebClientDeliverer(url));
comment|// }
comment|// String converter = manager.getProperty(cname + ".converter");
comment|// if (converter != null) {
comment|// engine.setConverter(createConverter(converter));
comment|// } else {
comment|// // default
comment|// engine.setConverter(new ContentSingleEntryConverter());
comment|// }
comment|// engine.setBatchSize(toInt(manager.getProperty(cname + ".batchSize"), 1, 1));
comment|// String retryType = manager.getProperty(cname + ".retry.pause");
comment|// if (retryType != null) {
comment|// int timeout = toInt(manager.getProperty(cname + ".retry.timeout"), 0, 0);
comment|// int pause = toInt(manager.getProperty(cname + ".retry.pause.time"), 1, 30);
comment|// boolean linear = !retryType.equalsIgnoreCase("exponential");
comment|// Deliverer wrapped = new RetryingDeliverer(engine.getDeliverer(), timeout, pause, linear);
comment|// engine.setDeliverer(wrapped);
comment|// }
comment|// }
specifier|private
name|void
name|configure2
parameter_list|()
block|{
name|LogManager
name|manager
init|=
name|LogManager
operator|.
name|getLogManager
argument_list|()
decl_stmt|;
name|String
name|cname
init|=
name|getClass
argument_list|()
operator|.
name|getName
argument_list|()
decl_stmt|;
name|AtomPushEngineConfigurator
name|conf
init|=
operator|new
name|AtomPushEngineConfigurator
argument_list|()
decl_stmt|;
name|conf
operator|.
name|setUrl
argument_list|(
name|manager
operator|.
name|getProperty
argument_list|(
name|cname
operator|+
literal|".url"
argument_list|)
argument_list|)
expr_stmt|;
name|conf
operator|.
name|setDelivererClass
argument_list|(
name|manager
operator|.
name|getProperty
argument_list|(
name|cname
operator|+
literal|".deliverer"
argument_list|)
argument_list|)
expr_stmt|;
name|conf
operator|.
name|setConverterClass
argument_list|(
name|manager
operator|.
name|getProperty
argument_list|(
name|cname
operator|+
literal|".converter"
argument_list|)
argument_list|)
expr_stmt|;
name|conf
operator|.
name|setBatchSize
argument_list|(
name|manager
operator|.
name|getProperty
argument_list|(
name|cname
operator|+
literal|".batchSize"
argument_list|)
argument_list|)
expr_stmt|;
name|conf
operator|.
name|setRetryPauseType
argument_list|(
name|manager
operator|.
name|getProperty
argument_list|(
name|cname
operator|+
literal|".retry.pause"
argument_list|)
argument_list|)
expr_stmt|;
name|conf
operator|.
name|setRetryPauseTime
argument_list|(
name|manager
operator|.
name|getProperty
argument_list|(
name|cname
operator|+
literal|".retry.pause.time"
argument_list|)
argument_list|)
expr_stmt|;
name|conf
operator|.
name|setRetryTimeout
argument_list|(
name|manager
operator|.
name|getProperty
argument_list|(
name|cname
operator|+
literal|".retry.timeout"
argument_list|)
argument_list|)
expr_stmt|;
name|engine
operator|=
name|conf
operator|.
name|createEngine
argument_list|()
expr_stmt|;
block|}
comment|//    private int toInt(String property, int defaultValue) {
comment|//        try {
comment|//            return Integer.parseInt(property);
comment|//        } catch (NumberFormatException e) {
comment|//            return defaultValue;
comment|//        }
comment|//    }
comment|//
comment|//    private int toInt(String property, int lessThan, int defaultValue) {
comment|//        int ret = toInt(property, defaultValue);
comment|//        if (ret< lessThan) {
comment|//            ret = defaultValue;
comment|//        }
comment|//        return ret;
comment|//    }
comment|//
comment|//    private Deliverer createDeliverer(String clazz, String url) {
comment|//        try {
comment|//            Constructor<?> ctor = loadClass(clazz).getConstructor(String.class);
comment|//            return (Deliverer)ctor.newInstance(url);
comment|//        } catch (Exception e) {
comment|//            throw new IllegalArgumentException(e);
comment|//        }
comment|//    }
comment|//
comment|//    private Converter createConverter(String clazz) {
comment|//        try {
comment|//            Constructor<?> ctor = loadClass(clazz).getConstructor();
comment|//            return (Converter)ctor.newInstance();
comment|//        } catch (Exception e) {
comment|//            throw new IllegalArgumentException(e);
comment|//        }
comment|//    }
comment|//
comment|//    private Class<?> loadClass(String clazz) throws ClassNotFoundException {
comment|//        try {
comment|//            return getClass().getClassLoader().loadClass(clazz);
comment|//        } catch (ClassNotFoundException e) {
comment|//            try {
comment|//                // clazz could be shorted (stripped package name) retry
comment|//                String clazz2 = getClass().getPackage().getName() + "." + clazz;
comment|//                return getClass().getClassLoader().loadClass(clazz2);
comment|//            } catch (Exception e1) {
comment|//                throw new ClassNotFoundException(e.getMessage() + " or " + e1.getMessage());
comment|//            }
comment|//        }
comment|//    }
block|}
end_class

end_unit

