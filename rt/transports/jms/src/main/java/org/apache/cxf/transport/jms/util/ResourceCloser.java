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
name|Connection
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

begin_import
import|import
name|javax
operator|.
name|naming
operator|.
name|Context
import|;
end_import

begin_class
specifier|public
class|class
name|ResourceCloser
implements|implements
name|Closeable
implements|,
name|AutoCloseable
block|{
specifier|private
name|AbstractSequentialList
argument_list|<
name|Object
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
argument_list|<>
argument_list|()
expr_stmt|;
block|}
specifier|public
parameter_list|<
name|E
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
annotation|@
name|Override
specifier|public
name|void
name|close
parameter_list|()
block|{
for|for
control|(
name|Object
name|resource
range|:
name|resources
control|)
block|{
name|close
argument_list|(
name|resource
argument_list|)
expr_stmt|;
block|}
name|resources
operator|.
name|clear
argument_list|()
expr_stmt|;
block|}
specifier|public
name|void
name|close
parameter_list|(
name|Object
modifier|...
name|resources2
parameter_list|)
block|{
for|for
control|(
name|Object
name|resource
range|:
name|resources2
control|)
block|{
name|close
argument_list|(
name|resource
argument_list|)
expr_stmt|;
block|}
block|}
specifier|public
specifier|static
name|void
name|close
parameter_list|(
name|Object
name|resource
parameter_list|)
block|{
if|if
condition|(
name|resource
operator|==
literal|null
condition|)
block|{
return|return;
block|}
try|try
block|{
if|if
condition|(
name|resource
operator|instanceof
name|MessageProducer
condition|)
block|{
operator|(
operator|(
name|MessageProducer
operator|)
name|resource
operator|)
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|resource
operator|instanceof
name|MessageConsumer
condition|)
block|{
operator|(
operator|(
name|MessageConsumer
operator|)
name|resource
operator|)
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|resource
operator|instanceof
name|Session
condition|)
block|{
operator|(
operator|(
name|Session
operator|)
name|resource
operator|)
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|resource
operator|instanceof
name|Connection
condition|)
block|{
operator|(
operator|(
name|Connection
operator|)
name|resource
operator|)
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|resource
operator|instanceof
name|Context
condition|)
block|{
operator|(
operator|(
name|Context
operator|)
name|resource
operator|)
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
else|else
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Can not handle resource "
operator|+
name|resource
operator|.
name|getClass
argument_list|()
argument_list|)
throw|;
block|}
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
end_class

end_unit

