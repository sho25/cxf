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
name|InputStream
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
name|Consumes
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
name|POST
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
name|Produces
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
name|SparkConf
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
name|Durations
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
name|StreamingContext
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
name|dstream
operator|.
name|ReceiverInputDStream
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
name|receiver
operator|.
name|Receiver
import|;
end_import

begin_import
import|import
name|scala
operator|.
name|reflect
operator|.
name|ClassTag
import|;
end_import

begin_comment
comment|// INCOMPLETE
end_comment

begin_class
annotation|@
name|Path
argument_list|(
literal|"/"
argument_list|)
specifier|public
class|class
name|AdvancedStreamingService
block|{
specifier|private
name|JavaStreamingContext
name|jssc
decl_stmt|;
specifier|public
name|AdvancedStreamingService
parameter_list|(
name|SparkConf
name|sparkConf
parameter_list|)
block|{
name|this
operator|.
name|jssc
operator|=
operator|new
name|JavaStreamingContext
argument_list|(
name|sparkConf
argument_list|,
name|Durations
operator|.
name|seconds
argument_list|(
literal|1
argument_list|)
argument_list|)
expr_stmt|;
operator|new
name|MyReceiverInputDStream
argument_list|(
name|jssc
operator|.
name|ssc
argument_list|()
argument_list|,
name|scala
operator|.
name|reflect
operator|.
name|ClassTag$
operator|.
name|MODULE$
operator|.
name|apply
argument_list|(
name|String
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|POST
annotation|@
name|Path
argument_list|(
literal|"/stream"
argument_list|)
annotation|@
name|Consumes
argument_list|(
literal|"text/plain"
argument_list|)
annotation|@
name|Produces
argument_list|(
literal|"text/plain"
argument_list|)
specifier|public
name|StreamingOutput
name|getStream
parameter_list|(
name|InputStream
name|is
parameter_list|)
block|{
return|return
literal|null
return|;
block|}
specifier|public
specifier|static
class|class
name|MyReceiverInputDStream
extends|extends
name|ReceiverInputDStream
argument_list|<
name|String
argument_list|>
block|{
specifier|public
name|MyReceiverInputDStream
parameter_list|(
name|StreamingContext
name|ssc
parameter_list|,
name|ClassTag
argument_list|<
name|String
argument_list|>
name|evidence
parameter_list|)
block|{
name|super
argument_list|(
name|ssc
argument_list|,
name|evidence
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|putInputStream
parameter_list|(
name|InputStream
name|is
parameter_list|)
block|{                      }
annotation|@
name|Override
specifier|public
name|Receiver
argument_list|<
name|String
argument_list|>
name|getReceiver
parameter_list|()
block|{
comment|// A receiver can be created per every String the input stream
return|return
operator|new
name|InputStreamReceiver
argument_list|(
name|getInputStream
argument_list|()
argument_list|)
return|;
block|}
specifier|public
name|InputStream
name|getInputStream
parameter_list|()
block|{
comment|// TODO Auto-generated method stub
return|return
literal|null
return|;
block|}
block|}
block|}
end_class

end_unit

