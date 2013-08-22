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
name|util
operator|.
name|logging
operator|.
name|Logger
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
name|Bus
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
name|common
operator|.
name|i18n
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
name|common
operator|.
name|injection
operator|.
name|NoJSR250Annotations
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
name|common
operator|.
name|logging
operator|.
name|LogUtils
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
name|configuration
operator|.
name|ConfigurationException
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
name|endpoint
operator|.
name|Client
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
name|endpoint
operator|.
name|Server
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
name|feature
operator|.
name|AbstractFeature
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
name|transport
operator|.
name|Conduit
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
name|transport
operator|.
name|Destination
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|beans
operator|.
name|factory
operator|.
name|annotation
operator|.
name|Required
import|;
end_import

begin_comment
comment|/**  * Allows to configure the JMSConfiguration directly at the Client or Server. Simply add this class to the  * Features and reference a JMSConfiguration. The configuration inside this class takes precedence over a  * configuration that is generated from the old configuration style.  */
end_comment

begin_class
annotation|@
name|NoJSR250Annotations
specifier|public
class|class
name|JMSConfigFeature
extends|extends
name|AbstractFeature
block|{
specifier|static
specifier|final
name|Logger
name|LOG
init|=
name|LogUtils
operator|.
name|getL7dLogger
argument_list|(
name|JMSConfigFeature
operator|.
name|class
argument_list|)
decl_stmt|;
name|JMSConfiguration
name|jmsConfig
decl_stmt|;
annotation|@
name|Override
specifier|public
name|void
name|initialize
parameter_list|(
name|Client
name|client
parameter_list|,
name|Bus
name|bus
parameter_list|)
block|{
name|checkJmsConfig
argument_list|()
expr_stmt|;
name|Conduit
name|conduit
init|=
name|client
operator|.
name|getConduit
argument_list|()
decl_stmt|;
if|if
condition|(
operator|!
operator|(
name|conduit
operator|instanceof
name|JMSConduit
operator|)
condition|)
block|{
throw|throw
operator|new
name|ConfigurationException
argument_list|(
operator|new
name|Message
argument_list|(
literal|"JMSCONFIGFEATURE_ONLY_JMS"
argument_list|,
name|LOG
argument_list|)
argument_list|)
throw|;
block|}
name|JMSConduit
name|jmsConduit
init|=
operator|(
name|JMSConduit
operator|)
name|conduit
decl_stmt|;
name|jmsConduit
operator|.
name|setJmsConfig
argument_list|(
name|jmsConfig
argument_list|)
expr_stmt|;
name|super
operator|.
name|initialize
argument_list|(
name|client
argument_list|,
name|bus
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|initialize
parameter_list|(
name|Server
name|server
parameter_list|,
name|Bus
name|bus
parameter_list|)
block|{
name|checkJmsConfig
argument_list|()
expr_stmt|;
name|Destination
name|destination
init|=
name|server
operator|.
name|getDestination
argument_list|()
decl_stmt|;
if|if
condition|(
operator|!
operator|(
name|destination
operator|instanceof
name|JMSDestination
operator|)
condition|)
block|{
throw|throw
operator|new
name|ConfigurationException
argument_list|(
operator|new
name|Message
argument_list|(
literal|"JMSCONFIGFEATURE_ONLY_JMS"
argument_list|,
name|LOG
argument_list|)
argument_list|)
throw|;
block|}
name|JMSDestination
name|jmsDestination
init|=
operator|(
name|JMSDestination
operator|)
name|destination
decl_stmt|;
name|jmsDestination
operator|.
name|setJmsConfig
argument_list|(
name|jmsConfig
argument_list|)
expr_stmt|;
name|super
operator|.
name|initialize
argument_list|(
name|server
argument_list|,
name|bus
argument_list|)
expr_stmt|;
block|}
specifier|public
name|JMSConfiguration
name|getJmsConfig
parameter_list|()
block|{
return|return
name|jmsConfig
return|;
block|}
annotation|@
name|Required
specifier|public
name|void
name|setJmsConfig
parameter_list|(
name|JMSConfiguration
name|jmsConfig
parameter_list|)
block|{
name|this
operator|.
name|jmsConfig
operator|=
name|jmsConfig
expr_stmt|;
block|}
specifier|private
name|void
name|checkJmsConfig
parameter_list|()
block|{
if|if
condition|(
name|jmsConfig
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|ConfigurationException
argument_list|(
operator|new
name|Message
argument_list|(
literal|"JMSCONFIG_REQUIRED"
argument_list|,
name|LOG
argument_list|)
argument_list|)
throw|;
block|}
block|}
block|}
end_class

end_unit

