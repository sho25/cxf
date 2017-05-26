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
package|;
end_package

begin_import
import|import
name|java
operator|.
name|security
operator|.
name|Principal
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
name|Message
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
name|security
operator|.
name|SecurityContext
import|;
end_import

begin_class
specifier|public
specifier|final
class|class
name|SecurityContextFactory
block|{
specifier|private
name|SecurityContextFactory
parameter_list|()
block|{     }
comment|/**      * Extract the property JMSXUserID or JMS_TIBCO_SENDER from the jms message and      * create a SecurityContext from it.      * For more info see Jira Issue CXF-2055      * {@link https://issues.apache.org/jira/browse/CXF-2055}      *      * @param message jms message to retrieve user information from      * @return SecurityContext that contains the user of the producer of the message as the Principal      * @throws JMSException if something goes wrong      */
specifier|public
specifier|static
name|SecurityContext
name|buildSecurityContext
parameter_list|(
name|Message
name|message
parameter_list|,
name|JMSConfiguration
name|config
parameter_list|)
throws|throws
name|JMSException
block|{
name|String
name|tempUserName
init|=
name|message
operator|.
name|getStringProperty
argument_list|(
literal|"JMSXUserID"
argument_list|)
decl_stmt|;
if|if
condition|(
name|tempUserName
operator|==
literal|null
operator|&&
name|config
operator|.
name|isJmsProviderTibcoEms
argument_list|()
condition|)
block|{
name|tempUserName
operator|=
name|message
operator|.
name|getStringProperty
argument_list|(
literal|"JMS_TIBCO_SENDER"
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|tempUserName
operator|==
literal|null
condition|)
block|{
return|return
literal|null
return|;
block|}
specifier|final
name|String
name|jmsUserName
init|=
name|tempUserName
decl_stmt|;
specifier|final
name|Principal
name|principal
init|=
operator|new
name|Principal
argument_list|()
block|{
specifier|public
name|String
name|getName
parameter_list|()
block|{
return|return
name|jmsUserName
return|;
block|}
block|}
decl_stmt|;
return|return
operator|new
name|SecurityContext
argument_list|()
block|{
specifier|public
name|Principal
name|getUserPrincipal
parameter_list|()
block|{
return|return
name|principal
return|;
block|}
specifier|public
name|boolean
name|isUserInRole
parameter_list|(
name|String
name|role
parameter_list|)
block|{
return|return
literal|false
return|;
block|}
block|}
return|;
block|}
block|}
end_class

end_unit
