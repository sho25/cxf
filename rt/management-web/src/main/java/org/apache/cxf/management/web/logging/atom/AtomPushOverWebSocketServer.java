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
name|management
operator|.
name|web
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
name|io
operator|.
name|IOException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Collections
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|HashMap
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Iterator
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Map
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
name|Handler
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|ws
operator|.
name|rs
operator|.
name|GET
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|ws
operator|.
name|rs
operator|.
name|HeaderParam
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|ws
operator|.
name|rs
operator|.
name|Path
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|ws
operator|.
name|rs
operator|.
name|PathParam
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|ws
operator|.
name|rs
operator|.
name|Produces
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|abdera
operator|.
name|model
operator|.
name|Element
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|abdera
operator|.
name|model
operator|.
name|Feed
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|commons
operator|.
name|lang
operator|.
name|Validate
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
name|StreamingResponse
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
name|management
operator|.
name|web
operator|.
name|logging
operator|.
name|atom
operator|.
name|converter
operator|.
name|Converter
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
name|management
operator|.
name|web
operator|.
name|logging
operator|.
name|atom
operator|.
name|deliverer
operator|.
name|Deliverer
import|;
end_import

begin_comment
comment|/**  * Bean used to configure {@link AtomPushHandler JUL handler} with Spring instead of properties file. See  * {@link AtomPushHandler} class for detailed description of parameters. Next to configuration of handler,  * Spring bean offers simple configuration of associated loggers that share ATOM push-style handler.  *<p>  * General rules:  *<ul>  *<li>When {@link #setConverter(Converter) converter} property is not set explicitly, default converter is  * created.</li>  *<li>When {@link #setLoggers(String) loggers} property is used, it overrides pair of  * {@link #setLogger(String) logger} and {@link #setLevel(String) level} properties; and vice versa.</li>  *<li>When logger is not set, handler is attached to root logger (named ""); when level is not set for  * logger, default "INFO" level is used.</li>  *<li>When {@link #setBatchSize(String) batchSize} property is not set or set to wrong value, default batch  * size of "1" is used.</li>  *<li>When deliverer property is NOT set, use of "retryXxx" properties causes creation of retrying default  * deliverer.</li>  *</ul>  * Examples:  *<p>  * ATOM push handler with registered with root logger for all levels or log events, pushing one feed per event  * over the connected websocket, using default conversion methods:  *   *<pre>  *&lt;bean class=&quot;org.apache.cxf.jaxrs.ext.logging.atom.AtomPushOverWebSocketBean&quot;   *     init-method=&quot;init&quot;&gt;  *&lt;property name=&quot;level&quot; value=&quot;ALL&quot; /&gt;  *&lt;/bean&gt;  *</pre>  *   * ATOM push handler registered with multiple loggers and listening for different levels (see  * {@link #setLoggers(String) loggers} property description for syntax details). Custom deliverer will take  * care of feeds, each of which carries batch of 10 log events:  *   *<pre>  *   ...  *&lt;bean class=&quot;org.apache.cxf.jaxrs.ext.logging.atom.AtomPushOverWebSocketServer&quot;   *     init-method=&quot;init&quot;&gt;  *&lt;property name=&quot;loggers&quot; value=&quot;  *           org.apache.cxf:DEBUG,  *           org.apache.cxf.jaxrs,  *           org.apache.cxf.bus:ERROR&quot; /&gt;  *&lt;property name=&quot;batchSize&quot; value=&quot;10&quot; /&gt;  *&lt;/bean&gt;  *</pre>  */
end_comment

begin_comment
comment|//REVISIT we will move the common part into AbstractAtomPushBean so that it can be shared by both AtomPushBean and this
end_comment

begin_class
annotation|@
name|Path
argument_list|(
literal|"/logs2"
argument_list|)
specifier|public
specifier|final
class|class
name|AtomPushOverWebSocketServer
extends|extends
name|AbstractAtomBean
block|{
specifier|private
name|AtomPushEngineConfigurator
name|conf
init|=
operator|new
name|AtomPushEngineConfigurator
argument_list|()
decl_stmt|;
specifier|private
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|activeStreams
decl_stmt|;
comment|/**      * Creates unconfigured and uninitialized bean. To configure setters must be used, then {@link #init()}      * must be called.      */
specifier|public
name|AtomPushOverWebSocketServer
parameter_list|()
block|{
name|conf
operator|.
name|setDeliverer
argument_list|(
operator|new
name|WebSocketDeliverer
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|init
parameter_list|()
block|{
name|super
operator|.
name|init
argument_list|()
expr_stmt|;
name|activeStreams
operator|=
name|Collections
operator|.
name|synchronizedMap
argument_list|(
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
argument_list|()
argument_list|)
expr_stmt|;
block|}
comment|/**      * Set initialized converter.      */
specifier|public
name|void
name|setConverter
parameter_list|(
name|Converter
name|converter
parameter_list|)
block|{
name|checkInit
argument_list|()
expr_stmt|;
name|Validate
operator|.
name|notNull
argument_list|(
name|converter
argument_list|,
literal|"converter is null"
argument_list|)
expr_stmt|;
name|conf
operator|.
name|setConverter
argument_list|(
name|converter
argument_list|)
expr_stmt|;
block|}
comment|/**      * Size of batch; empty string for default one element batch.      */
specifier|public
name|void
name|setBatchSize
parameter_list|(
name|String
name|batchSize
parameter_list|)
block|{
name|checkInit
argument_list|()
expr_stmt|;
name|Validate
operator|.
name|notNull
argument_list|(
name|batchSize
argument_list|,
literal|"batchSize is null"
argument_list|)
expr_stmt|;
name|conf
operator|.
name|setBatchSize
argument_list|(
name|batchSize
argument_list|)
expr_stmt|;
block|}
comment|/**      * Batch cleanup time in minutes      */
specifier|public
name|void
name|setBatchCleanupTime
parameter_list|(
name|String
name|batchCleanupTime
parameter_list|)
block|{
name|checkInit
argument_list|()
expr_stmt|;
name|Validate
operator|.
name|notNull
argument_list|(
name|batchCleanupTime
argument_list|,
literal|"batchCleanup is null"
argument_list|)
expr_stmt|;
name|conf
operator|.
name|setBatchCleanupTime
argument_list|(
name|batchCleanupTime
argument_list|)
expr_stmt|;
block|}
comment|/**      * Retry pause calculation strategy, either "linear" or "exponential".      */
specifier|public
name|void
name|setRetryPause
parameter_list|(
name|String
name|retryPause
parameter_list|)
block|{
name|checkInit
argument_list|()
expr_stmt|;
name|Validate
operator|.
name|notNull
argument_list|(
name|retryPause
argument_list|,
literal|"retryPause is null"
argument_list|)
expr_stmt|;
name|conf
operator|.
name|setRetryPause
argument_list|(
name|retryPause
argument_list|)
expr_stmt|;
block|}
comment|/**      * Retry pause time (in seconds).      */
specifier|public
name|void
name|setRetryPauseTime
parameter_list|(
name|String
name|time
parameter_list|)
block|{
name|checkInit
argument_list|()
expr_stmt|;
name|Validate
operator|.
name|notNull
argument_list|(
name|time
argument_list|,
literal|"time is null"
argument_list|)
expr_stmt|;
name|conf
operator|.
name|setRetryPauseTime
argument_list|(
name|time
argument_list|)
expr_stmt|;
block|}
comment|/**      * Retry timeout (in seconds).      */
specifier|public
name|void
name|setRetryTimeout
parameter_list|(
name|String
name|timeout
parameter_list|)
block|{
name|checkInit
argument_list|()
expr_stmt|;
name|Validate
operator|.
name|notNull
argument_list|(
name|timeout
argument_list|,
literal|"timeout is null"
argument_list|)
expr_stmt|;
name|conf
operator|.
name|setRetryTimeout
argument_list|(
name|timeout
argument_list|)
expr_stmt|;
block|}
comment|/**      * Conversion output type: "feed" or "entry".      */
specifier|public
name|void
name|setOutput
parameter_list|(
name|String
name|output
parameter_list|)
block|{
name|checkInit
argument_list|()
expr_stmt|;
name|Validate
operator|.
name|notNull
argument_list|(
name|output
argument_list|,
literal|"output is null"
argument_list|)
expr_stmt|;
name|conf
operator|.
name|setOutput
argument_list|(
name|output
argument_list|)
expr_stmt|;
block|}
comment|/**      * Multiplicity of subelement of output: "one" or "many".      */
specifier|public
name|void
name|setMultiplicity
parameter_list|(
name|String
name|multiplicity
parameter_list|)
block|{
name|checkInit
argument_list|()
expr_stmt|;
name|Validate
operator|.
name|notNull
argument_list|(
name|multiplicity
argument_list|,
literal|"multiplicity is null"
argument_list|)
expr_stmt|;
name|conf
operator|.
name|setMultiplicity
argument_list|(
name|multiplicity
argument_list|)
expr_stmt|;
block|}
comment|/**      * Entry data format: "content" or "extension".      */
specifier|public
name|void
name|setFormat
parameter_list|(
name|String
name|format
parameter_list|)
block|{
name|checkInit
argument_list|()
expr_stmt|;
name|Validate
operator|.
name|notNull
argument_list|(
name|format
argument_list|,
literal|"format is null"
argument_list|)
expr_stmt|;
name|conf
operator|.
name|setFormat
argument_list|(
name|format
argument_list|)
expr_stmt|;
block|}
specifier|protected
name|Handler
name|createHandler
parameter_list|()
block|{
return|return
operator|new
name|AtomPushHandler
argument_list|(
name|conf
operator|.
name|createEngine
argument_list|()
argument_list|)
return|;
block|}
annotation|@
name|GET
annotation|@
name|Produces
argument_list|(
literal|"application/atom+xml"
argument_list|)
annotation|@
name|Path
argument_list|(
literal|"subscribe"
argument_list|)
specifier|public
name|StreamingResponse
argument_list|<
name|Feed
argument_list|>
name|subscribeXmlFeed
parameter_list|(
annotation|@
name|HeaderParam
argument_list|(
literal|"requestId"
argument_list|)
name|String
name|reqid
parameter_list|)
block|{
specifier|final
name|String
name|key
init|=
name|reqid
operator|==
literal|null
condition|?
literal|"*"
else|:
name|reqid
decl_stmt|;
return|return
operator|new
name|StreamingResponse
argument_list|<
name|Feed
argument_list|>
argument_list|()
block|{
specifier|public
name|void
name|writeTo
parameter_list|(
specifier|final
name|StreamingResponse
operator|.
name|Writer
argument_list|<
name|Feed
argument_list|>
name|out
parameter_list|)
throws|throws
name|IOException
block|{
name|activeStreams
operator|.
name|put
argument_list|(
name|key
argument_list|,
name|out
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
annotation|@
name|GET
annotation|@
name|Produces
argument_list|(
literal|"text/plain"
argument_list|)
annotation|@
name|Path
argument_list|(
literal|"unsubscribe/{key}"
argument_list|)
specifier|public
name|Boolean
name|unsubscribeXmlFeed
parameter_list|(
annotation|@
name|PathParam
argument_list|(
literal|"key"
argument_list|)
name|String
name|key
parameter_list|)
block|{
return|return
name|activeStreams
operator|.
name|remove
argument_list|(
name|key
argument_list|)
operator|!=
literal|null
return|;
block|}
specifier|private
class|class
name|WebSocketDeliverer
implements|implements
name|Deliverer
block|{
annotation|@
name|Override
specifier|public
name|boolean
name|deliver
parameter_list|(
name|Element
name|element
parameter_list|)
throws|throws
name|InterruptedException
block|{
if|if
condition|(
name|activeStreams
operator|.
name|size
argument_list|()
operator|>
literal|0
condition|)
block|{
for|for
control|(
name|Iterator
argument_list|<
name|Object
argument_list|>
name|it
init|=
name|activeStreams
operator|.
name|values
argument_list|()
operator|.
name|iterator
argument_list|()
init|;
name|it
operator|.
name|hasNext
argument_list|()
condition|;
control|)
block|{
name|Object
name|out
init|=
name|it
operator|.
name|next
argument_list|()
decl_stmt|;
try|try
block|{
if|if
condition|(
name|out
operator|instanceof
name|StreamingResponse
operator|.
name|Writer
condition|)
block|{
operator|(
operator|(
name|StreamingResponse
operator|.
name|Writer
operator|)
name|out
operator|)
operator|.
name|write
argument_list|(
name|element
argument_list|)
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|Throwable
name|t
parameter_list|)
block|{
comment|// REVISIT
comment|// the reason for not logging anything here is to not further clog the logger
comment|// with this log broadcasting failure.
name|System
operator|.
name|err
operator|.
name|print
argument_list|(
literal|"ERROR | AtomPushOverWebSocketServer | "
operator|+
name|t
operator|+
literal|"; Unregistering "
operator|+
name|out
argument_list|)
expr_stmt|;
name|it
operator|.
name|remove
argument_list|()
expr_stmt|;
block|}
block|}
block|}
return|return
literal|true
return|;
block|}
annotation|@
name|Override
specifier|public
name|String
name|getEndpointAddress
parameter_list|()
block|{
comment|//REVISIT return something else?
return|return
literal|null
return|;
block|}
block|}
block|}
end_class

end_unit

