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
name|transport
operator|.
name|jms
operator|.
name|util
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|Closeable
import|;
end_import

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
name|AbstractSequentialList
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|LinkedList
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|jms
operator|.
name|JMSException
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|jms
operator|.
name|MessageConsumer
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|jms
operator|.
name|MessageProducer
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|jms
operator|.
name|Session
import|;
end_import

begin_class
specifier|public
class|class
name|ResourceCloser
implements|implements
name|Closeable
block|{
specifier|private
name|AbstractSequentialList
argument_list|<
name|Closeable
argument_list|>
name|resources
decl_stmt|;
specifier|public
name|ResourceCloser
parameter_list|()
block|{
name|resources
operator|=
operator|new
name|LinkedList
argument_list|<
name|Closeable
argument_list|>
argument_list|()
expr_stmt|;
block|}
specifier|public
parameter_list|<
name|E
extends|extends
name|Closeable
parameter_list|>
name|E
name|register
parameter_list|(
name|E
name|resource
parameter_list|)
block|{
name|resources
operator|.
name|add
argument_list|(
literal|0
argument_list|,
name|resource
argument_list|)
expr_stmt|;
return|return
name|resource
return|;
block|}
specifier|public
name|javax
operator|.
name|jms
operator|.
name|Connection
name|register
parameter_list|(
specifier|final
name|javax
operator|.
name|jms
operator|.
name|Connection
name|connection
parameter_list|)
block|{
name|resources
operator|.
name|add
argument_list|(
literal|0
argument_list|,
operator|new
name|Closeable
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|void
name|close
parameter_list|()
throws|throws
name|IOException
block|{
try|try
block|{
name|connection
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|JMSException
name|e
parameter_list|)
block|{
comment|// Ignore
block|}
block|}
block|}
argument_list|)
expr_stmt|;
return|return
name|connection
return|;
block|}
specifier|public
name|Session
name|register
parameter_list|(
specifier|final
name|Session
name|session
parameter_list|)
block|{
name|resources
operator|.
name|add
argument_list|(
literal|0
argument_list|,
operator|new
name|Closeable
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|void
name|close
parameter_list|()
throws|throws
name|IOException
block|{
try|try
block|{
name|session
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|JMSException
name|e
parameter_list|)
block|{
comment|// Ignore
block|}
block|}
block|}
argument_list|)
expr_stmt|;
return|return
name|session
return|;
block|}
specifier|public
name|MessageConsumer
name|register
parameter_list|(
specifier|final
name|MessageConsumer
name|consumer
parameter_list|)
block|{
name|resources
operator|.
name|add
argument_list|(
literal|0
argument_list|,
operator|new
name|Closeable
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|void
name|close
parameter_list|()
throws|throws
name|IOException
block|{
try|try
block|{
name|consumer
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|JMSException
name|e
parameter_list|)
block|{
comment|// Ignore
block|}
block|}
block|}
argument_list|)
expr_stmt|;
return|return
name|consumer
return|;
block|}
specifier|public
name|MessageProducer
name|register
parameter_list|(
specifier|final
name|MessageProducer
name|producer
parameter_list|)
block|{
name|resources
operator|.
name|add
argument_list|(
literal|0
argument_list|,
operator|new
name|Closeable
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|void
name|close
parameter_list|()
throws|throws
name|IOException
block|{
try|try
block|{
name|producer
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|JMSException
name|e
parameter_list|)
block|{
comment|// Ignore
block|}
block|}
block|}
argument_list|)
expr_stmt|;
return|return
name|producer
return|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|close
parameter_list|()
block|{
for|for
control|(
name|Closeable
name|resource
range|:
name|resources
control|)
block|{
try|try
block|{
name|resource
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
comment|// Ignore
block|}
block|}
block|}
block|}
end_class

end_unit

