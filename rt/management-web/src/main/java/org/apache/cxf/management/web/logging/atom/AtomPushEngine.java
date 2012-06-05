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
name|util
operator|.
name|ArrayList
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
name|List
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Timer
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|TimerTask
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|ExecutorService
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|Executors
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|TimeUnit
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
name|management
operator|.
name|web
operator|.
name|logging
operator|.
name|LogRecord
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
comment|/**  * Package private ATOM push-style engine. Engine enqueues log records as they are {@link #publish(LogRecord)  * published}. After queue size exceeds {@link #getBatchSize() batch size} processing of collection of these  * records (in size of batch size) is triggered.  *<p>  * Processing is done in separate thread not to block publishing interface. Processing is two step: first list  * of log records is transformed by {@link Converter converter} to ATOM {@link Element element} and then it is  * pushed out by {@link Deliverer deliverer} to client. Next to transport deliverer is indirectly responsible  * for marshaling ATOM element to XML.  *<p>  * Processing is done by single threaded {@link java.util.concurrent.Executor executor}; next batch of records  * is taken from queue only when currently processed batch finishes and queue has enough elements to proceed.  *<p>  * First failure of any delivery shuts engine down. To avoid this situation engine must have registered  * reliable deliverer or use wrapping  * {@link org.apache.cxf.jaxrs.ext.logging.atom.deliverer.RetryingDeliverer}.  */
end_comment

begin_comment
comment|// TODO add internal diagnostics - log messages somewhere except for logger :D
end_comment

begin_class
specifier|final
class|class
name|AtomPushEngine
block|{
specifier|private
name|List
argument_list|<
name|LogRecord
argument_list|>
name|queue
init|=
operator|new
name|ArrayList
argument_list|<
name|LogRecord
argument_list|>
argument_list|()
decl_stmt|;
specifier|private
name|ExecutorService
name|executor
init|=
name|Executors
operator|.
name|newSingleThreadExecutor
argument_list|()
decl_stmt|;
specifier|private
name|int
name|batchSize
init|=
literal|1
decl_stmt|;
specifier|private
name|int
name|batchTime
decl_stmt|;
specifier|private
name|Converter
name|converter
decl_stmt|;
specifier|private
name|Deliverer
name|deliverer
decl_stmt|;
specifier|private
name|Timer
name|timer
decl_stmt|;
comment|/**      * Put record to publishing queue. Engine accepts published records only if is in proper state - is      * properly configured (has deliverer and converter registered) and is not shot down; otherwise calls to      * publish are ignored.      *       * @param record record to be published.      */
specifier|public
specifier|synchronized
name|void
name|publish
parameter_list|(
name|LogRecord
name|record
parameter_list|)
block|{
name|Validate
operator|.
name|notNull
argument_list|(
name|record
argument_list|,
literal|"record is null"
argument_list|)
expr_stmt|;
if|if
condition|(
name|isValid
argument_list|()
condition|)
block|{
if|if
condition|(
name|batchSize
operator|>
literal|1
operator|&&
name|batchTime
operator|>
literal|0
operator|&&
name|timer
operator|==
literal|null
condition|)
block|{
name|createTimerTask
argument_list|(
name|batchTime
operator|*
literal|60
operator|*
literal|1000
argument_list|)
expr_stmt|;
block|}
name|queue
operator|.
name|add
argument_list|(
name|record
argument_list|)
expr_stmt|;
if|if
condition|(
name|queue
operator|.
name|size
argument_list|()
operator|>=
name|batchSize
condition|)
block|{
name|publishAndReset
argument_list|()
expr_stmt|;
block|}
block|}
else|else
block|{
name|handleUndeliveredRecords
argument_list|(
name|Collections
operator|.
name|singletonList
argument_list|(
name|record
argument_list|)
argument_list|,
name|deliverer
operator|==
literal|null
condition|?
literal|""
else|:
name|deliverer
operator|.
name|getEndpointAddress
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
specifier|protected
specifier|synchronized
name|void
name|publishAndReset
parameter_list|()
block|{
name|publishBatch
argument_list|(
name|queue
argument_list|,
name|deliverer
argument_list|,
name|converter
argument_list|)
expr_stmt|;
name|queue
operator|=
operator|new
name|ArrayList
argument_list|<
name|LogRecord
argument_list|>
argument_list|()
expr_stmt|;
block|}
comment|/**      * Shuts engine down.      */
specifier|public
specifier|synchronized
name|void
name|shutdown
parameter_list|()
block|{
name|cancelTimerTask
argument_list|()
expr_stmt|;
if|if
condition|(
name|isValid
argument_list|()
operator|&&
name|queue
operator|.
name|size
argument_list|()
operator|>
literal|0
condition|)
block|{
name|publishAndReset
argument_list|()
expr_stmt|;
block|}
name|executor
operator|.
name|shutdown
argument_list|()
expr_stmt|;
try|try
block|{
comment|//wait a little to try and flush the batches
comment|//it's not critical, but can avoid errors on the
comment|//console and such which could be confusing
name|executor
operator|.
name|awaitTermination
argument_list|(
literal|20
argument_list|,
name|TimeUnit
operator|.
name|SECONDS
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|InterruptedException
name|e
parameter_list|)
block|{
comment|//ignore
block|}
block|}
specifier|private
name|boolean
name|isValid
parameter_list|()
block|{
if|if
condition|(
name|deliverer
operator|==
literal|null
condition|)
block|{
comment|// TODO report cause
comment|///System.err.println("deliverer is not set");
return|return
literal|false
return|;
block|}
if|if
condition|(
name|converter
operator|==
literal|null
condition|)
block|{
comment|//System.err.println("converter is not set");
return|return
literal|false
return|;
block|}
if|if
condition|(
name|executor
operator|.
name|isShutdown
argument_list|()
condition|)
block|{
comment|//System.err.println("engine shutdown");
return|return
literal|false
return|;
block|}
return|return
literal|true
return|;
block|}
specifier|private
name|void
name|publishBatch
parameter_list|(
specifier|final
name|List
argument_list|<
name|LogRecord
argument_list|>
name|batch
parameter_list|,
specifier|final
name|Deliverer
name|d
parameter_list|,
specifier|final
name|Converter
name|c
parameter_list|)
block|{
name|executor
operator|.
name|execute
argument_list|(
operator|new
name|Runnable
argument_list|()
block|{
specifier|public
name|void
name|run
parameter_list|()
block|{
try|try
block|{
name|LoggingThread
operator|.
name|markSilent
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|?
extends|extends
name|Element
argument_list|>
name|elements
init|=
name|c
operator|.
name|convert
argument_list|(
name|batch
argument_list|)
decl_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|elements
operator|.
name|size
argument_list|()
condition|;
name|i
operator|++
control|)
block|{
name|Element
name|element
init|=
name|elements
operator|.
name|get
argument_list|(
name|i
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|d
operator|.
name|deliver
argument_list|(
name|element
argument_list|)
condition|)
block|{
name|System
operator|.
name|err
operator|.
name|println
argument_list|(
literal|"Delivery to "
operator|+
name|d
operator|.
name|getEndpointAddress
argument_list|()
operator|+
literal|" failed, shutting engine down"
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|LogRecord
argument_list|>
name|undelivered
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|i
operator|==
literal|0
condition|)
block|{
name|undelivered
operator|=
name|batch
expr_stmt|;
block|}
else|else
block|{
name|int
name|index
init|=
operator|(
name|batch
operator|.
name|size
argument_list|()
operator|/
name|elements
operator|.
name|size
argument_list|()
operator|)
operator|*
name|i
decl_stmt|;
comment|// should not happen but just in case :-)
if|if
condition|(
name|index
operator|<
name|batch
operator|.
name|size
argument_list|()
condition|)
block|{
name|undelivered
operator|=
name|batch
operator|.
name|subList
argument_list|(
name|index
argument_list|,
name|batch
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
name|handleUndeliveredRecords
argument_list|(
name|undelivered
argument_list|,
name|d
operator|.
name|getEndpointAddress
argument_list|()
argument_list|)
expr_stmt|;
name|shutdown
argument_list|()
expr_stmt|;
break|break;
block|}
block|}
block|}
catch|catch
parameter_list|(
name|InterruptedException
name|e
parameter_list|)
block|{
comment|// no action
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
block|}
argument_list|)
expr_stmt|;
block|}
specifier|protected
name|void
name|handleUndeliveredRecords
parameter_list|(
name|List
argument_list|<
name|LogRecord
argument_list|>
name|records
parameter_list|,
name|String
name|address
parameter_list|)
block|{
comment|// TODO : save them to some transient storage perhaps ?
name|System
operator|.
name|err
operator|.
name|println
argument_list|(
literal|"The following records have been undelivered to "
operator|+
name|address
operator|+
literal|" : "
argument_list|)
expr_stmt|;
for|for
control|(
name|LogRecord
name|r
range|:
name|records
control|)
block|{
name|System
operator|.
name|err
operator|.
name|println
argument_list|(
name|r
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
specifier|public
name|int
name|getBatchSize
parameter_list|()
block|{
return|return
name|batchSize
return|;
block|}
specifier|public
name|void
name|setBatchSize
parameter_list|(
name|int
name|batchSize
parameter_list|)
block|{
name|Validate
operator|.
name|isTrue
argument_list|(
name|batchSize
operator|>
literal|0
argument_list|,
literal|"batch size is not greater than zero"
argument_list|)
expr_stmt|;
name|this
operator|.
name|batchSize
operator|=
name|batchSize
expr_stmt|;
block|}
specifier|public
name|void
name|setBatchTime
parameter_list|(
name|int
name|batchTime
parameter_list|)
block|{
name|this
operator|.
name|batchTime
operator|=
name|batchTime
expr_stmt|;
block|}
comment|/**      * Creates a timer task which will periodically flush the batch queue      * thus ensuring log records won't become too 'stale'.       * Ex, if we have a batch size 10 and only WARN records need to be delivered      * then without the periodic cleanup the consumers may not get prompt notifications      *        * @param timeout      */
specifier|protected
name|void
name|createTimerTask
parameter_list|(
name|long
name|timeout
parameter_list|)
block|{
name|timer
operator|=
operator|new
name|Timer
argument_list|()
expr_stmt|;
name|timer
operator|.
name|schedule
argument_list|(
operator|new
name|TimerTask
argument_list|()
block|{
specifier|public
name|void
name|run
parameter_list|()
block|{
name|publishAndReset
argument_list|()
expr_stmt|;
block|}
block|}
argument_list|,
name|timeout
argument_list|)
expr_stmt|;
block|}
specifier|protected
name|void
name|cancelTimerTask
parameter_list|()
block|{
if|if
condition|(
name|timer
operator|!=
literal|null
condition|)
block|{
name|timer
operator|.
name|cancel
argument_list|()
expr_stmt|;
name|timer
operator|=
literal|null
expr_stmt|;
block|}
block|}
specifier|public
specifier|synchronized
name|Converter
name|getConverter
parameter_list|()
block|{
return|return
name|converter
return|;
block|}
specifier|public
specifier|synchronized
name|void
name|setConverter
parameter_list|(
name|Converter
name|converter
parameter_list|)
block|{
name|Validate
operator|.
name|notNull
argument_list|(
name|converter
argument_list|,
literal|"converter is null"
argument_list|)
expr_stmt|;
name|this
operator|.
name|converter
operator|=
name|converter
expr_stmt|;
block|}
specifier|public
specifier|synchronized
name|Deliverer
name|getDeliverer
parameter_list|()
block|{
return|return
name|deliverer
return|;
block|}
specifier|public
specifier|synchronized
name|void
name|setDeliverer
parameter_list|(
name|Deliverer
name|deliverer
parameter_list|)
block|{
name|Validate
operator|.
name|notNull
argument_list|(
name|deliverer
argument_list|,
literal|"deliverer is null"
argument_list|)
expr_stmt|;
name|this
operator|.
name|deliverer
operator|=
name|deliverer
expr_stmt|;
block|}
block|}
end_class

end_unit

