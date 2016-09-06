begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one  * or more contributor license agreements. See the NOTICE file  * distributed with this work for additional information  * regarding copyright ownership. The ASF licenses this file  * to you under the Apache License, Version 2.0 (the  * "License"); you may not use this file except in compliance  * with the License. You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing,  * software distributed under the License is distributed on an  * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  * KIND, either express or implied. See the License for the  * specific language governing permissions and limitations  * under the License.  */
end_comment

begin_package
package|package
name|demo
operator|.
name|jaxrs
operator|.
name|server
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
name|io
operator|.
name|OutputStream
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
name|javax
operator|.
name|ws
operator|.
name|rs
operator|.
name|WebApplicationException
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
name|core
operator|.
name|StreamingOutput
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|spark
operator|.
name|api
operator|.
name|java
operator|.
name|JavaPairRDD
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|spark
operator|.
name|api
operator|.
name|java
operator|.
name|function
operator|.
name|VoidFunction
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|spark
operator|.
name|streaming
operator|.
name|api
operator|.
name|java
operator|.
name|JavaPairDStream
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|spark
operator|.
name|streaming
operator|.
name|api
operator|.
name|java
operator|.
name|JavaStreamingContext
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|spark
operator|.
name|streaming
operator|.
name|scheduler
operator|.
name|StreamingListener
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|spark
operator|.
name|streaming
operator|.
name|scheduler
operator|.
name|StreamingListenerBatchCompleted
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|spark
operator|.
name|streaming
operator|.
name|scheduler
operator|.
name|StreamingListenerBatchStarted
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|spark
operator|.
name|streaming
operator|.
name|scheduler
operator|.
name|StreamingListenerBatchSubmitted
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|spark
operator|.
name|streaming
operator|.
name|scheduler
operator|.
name|StreamingListenerOutputOperationCompleted
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|spark
operator|.
name|streaming
operator|.
name|scheduler
operator|.
name|StreamingListenerOutputOperationStarted
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|spark
operator|.
name|streaming
operator|.
name|scheduler
operator|.
name|StreamingListenerReceiverError
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|spark
operator|.
name|streaming
operator|.
name|scheduler
operator|.
name|StreamingListenerReceiverStarted
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|spark
operator|.
name|streaming
operator|.
name|scheduler
operator|.
name|StreamingListenerReceiverStopped
import|;
end_import

begin_class
specifier|public
class|class
name|SparkStreamingOutput
implements|implements
name|StreamingOutput
block|{
specifier|private
name|JavaPairDStream
argument_list|<
name|String
argument_list|,
name|Integer
argument_list|>
name|wordCounts
decl_stmt|;
specifier|private
name|JavaStreamingContext
name|jssc
decl_stmt|;
specifier|private
name|boolean
name|sparkDone
decl_stmt|;
specifier|private
name|boolean
name|batchCompleted
decl_stmt|;
specifier|public
name|SparkStreamingOutput
parameter_list|(
name|JavaStreamingContext
name|jssc
parameter_list|,
name|JavaPairDStream
argument_list|<
name|String
argument_list|,
name|Integer
argument_list|>
name|wordCounts
parameter_list|)
block|{
name|this
operator|.
name|jssc
operator|=
name|jssc
expr_stmt|;
name|this
operator|.
name|wordCounts
operator|=
name|wordCounts
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|write
parameter_list|(
specifier|final
name|OutputStream
name|output
parameter_list|)
throws|throws
name|IOException
throws|,
name|WebApplicationException
block|{
name|wordCounts
operator|.
name|foreachRDD
argument_list|(
operator|new
name|OutputFunction
argument_list|(
name|output
argument_list|)
argument_list|)
expr_stmt|;
name|jssc
operator|.
name|addStreamingListener
argument_list|(
operator|new
name|SparkStreamingListener
argument_list|()
argument_list|)
expr_stmt|;
name|jssc
operator|.
name|start
argument_list|()
expr_stmt|;
name|awaitTermination
argument_list|()
expr_stmt|;
name|jssc
operator|.
name|stop
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|jssc
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
specifier|private
specifier|synchronized
name|void
name|awaitTermination
parameter_list|()
block|{
while|while
condition|(
operator|!
name|sparkDone
condition|)
block|{
try|try
block|{
name|wait
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|InterruptedException
name|e
parameter_list|)
block|{
return|return;
block|}
block|}
block|}
specifier|private
specifier|synchronized
name|void
name|releaseStreamingContext
parameter_list|()
block|{
if|if
condition|(
name|batchCompleted
condition|)
block|{
name|sparkDone
operator|=
literal|true
expr_stmt|;
name|notify
argument_list|()
expr_stmt|;
block|}
block|}
specifier|private
specifier|synchronized
name|void
name|setBatchCompleted
parameter_list|()
block|{
name|batchCompleted
operator|=
literal|true
expr_stmt|;
block|}
comment|// This dedicated class was introduced to validate that when Spark is running it does not
comment|// fail the processing due to OutputStream being one of the fields in the serializable class,
specifier|private
class|class
name|OutputFunction
implements|implements
name|VoidFunction
argument_list|<
name|JavaPairRDD
argument_list|<
name|String
argument_list|,
name|Integer
argument_list|>
argument_list|>
block|{
specifier|private
specifier|static
specifier|final
name|long
name|serialVersionUID
init|=
literal|1L
decl_stmt|;
specifier|private
name|OutputStream
name|os
decl_stmt|;
name|OutputFunction
parameter_list|(
name|OutputStream
name|os
parameter_list|)
block|{
name|this
operator|.
name|os
operator|=
name|os
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|call
parameter_list|(
name|JavaPairRDD
argument_list|<
name|String
argument_list|,
name|Integer
argument_list|>
name|rdd
parameter_list|)
block|{
for|for
control|(
name|Map
operator|.
name|Entry
argument_list|<
name|String
argument_list|,
name|Integer
argument_list|>
name|entry
range|:
name|rdd
operator|.
name|collectAsMap
argument_list|()
operator|.
name|entrySet
argument_list|()
control|)
block|{
name|String
name|value
init|=
name|entry
operator|.
name|getKey
argument_list|()
operator|+
literal|" : "
operator|+
name|entry
operator|.
name|getValue
argument_list|()
operator|+
literal|"\r\n"
decl_stmt|;
try|try
block|{
name|os
operator|.
name|write
argument_list|(
name|value
operator|.
name|getBytes
argument_list|()
argument_list|)
expr_stmt|;
name|os
operator|.
name|flush
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|ex
parameter_list|)
block|{
throw|throw
operator|new
name|WebApplicationException
argument_list|()
throw|;
block|}
block|}
comment|// Right now we assume by the time we call it the whole InputStream has been
comment|// processed
name|releaseStreamingContext
argument_list|()
expr_stmt|;
block|}
block|}
specifier|private
class|class
name|SparkStreamingListener
implements|implements
name|StreamingListener
block|{
annotation|@
name|Override
specifier|public
name|void
name|onBatchCompleted
parameter_list|(
name|StreamingListenerBatchCompleted
name|event
parameter_list|)
block|{
comment|// as soon as the batch is finished we let the streaming context go
comment|// but this may need to be revisited if a given InputStream happens to be processed in
comment|// multiple batches ?
name|setBatchCompleted
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|onBatchStarted
parameter_list|(
name|StreamingListenerBatchStarted
name|event
parameter_list|)
block|{         }
annotation|@
name|Override
specifier|public
name|void
name|onBatchSubmitted
parameter_list|(
name|StreamingListenerBatchSubmitted
name|event
parameter_list|)
block|{         }
annotation|@
name|Override
specifier|public
name|void
name|onOutputOperationCompleted
parameter_list|(
name|StreamingListenerOutputOperationCompleted
name|event
parameter_list|)
block|{         }
annotation|@
name|Override
specifier|public
name|void
name|onOutputOperationStarted
parameter_list|(
name|StreamingListenerOutputOperationStarted
name|event
parameter_list|)
block|{         }
annotation|@
name|Override
specifier|public
name|void
name|onReceiverError
parameter_list|(
name|StreamingListenerReceiverError
name|event
parameter_list|)
block|{         }
annotation|@
name|Override
specifier|public
name|void
name|onReceiverStarted
parameter_list|(
name|StreamingListenerReceiverStarted
name|event
parameter_list|)
block|{         }
annotation|@
name|Override
specifier|public
name|void
name|onReceiverStopped
parameter_list|(
name|StreamingListenerReceiverStopped
name|arg0
parameter_list|)
block|{         }
block|}
block|}
end_class

end_unit

