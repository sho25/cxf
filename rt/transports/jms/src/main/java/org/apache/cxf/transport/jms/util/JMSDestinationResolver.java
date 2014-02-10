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
name|javax
operator|.
name|jms
operator|.
name|Destination
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
name|Session
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|naming
operator|.
name|NamingException
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|jms
operator|.
name|support
operator|.
name|destination
operator|.
name|DestinationResolver
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|jndi
operator|.
name|JndiTemplate
import|;
end_import

begin_class
specifier|public
class|class
name|JMSDestinationResolver
implements|implements
name|DestinationResolver
block|{
name|JndiTemplate
name|jndiTemplate
decl_stmt|;
specifier|public
name|Destination
name|resolveDestinationName
parameter_list|(
name|Session
name|session
parameter_list|,
name|String
name|destinationName
parameter_list|,
name|boolean
name|pubSubDomain
parameter_list|)
throws|throws
name|JMSException
block|{
if|if
condition|(
name|jndiTemplate
operator|!=
literal|null
condition|)
block|{
try|try
block|{
return|return
name|jndiTemplate
operator|.
name|lookup
argument_list|(
name|destinationName
argument_list|,
name|Destination
operator|.
name|class
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|NamingException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|RuntimeException
argument_list|(
name|e
operator|.
name|getMessage
argument_list|()
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
if|if
condition|(
name|pubSubDomain
condition|)
block|{
return|return
name|session
operator|.
name|createTopic
argument_list|(
name|destinationName
argument_list|)
return|;
block|}
else|else
block|{
return|return
name|session
operator|.
name|createQueue
argument_list|(
name|destinationName
argument_list|)
return|;
block|}
block|}
specifier|public
name|void
name|setJndiTemplate
parameter_list|(
name|JndiTemplate
name|jt
parameter_list|)
block|{
name|this
operator|.
name|jndiTemplate
operator|=
name|jt
expr_stmt|;
block|}
block|}
end_class

end_unit

