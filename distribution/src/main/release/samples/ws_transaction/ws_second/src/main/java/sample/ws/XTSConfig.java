begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one  * or more contributor license agreements. See the NOTICE file  * distributed with this work for additional information  * regarding copyright ownership. The ASF licenses this file  * to you under the Apache License, Version 2.0 (the  * "License"); you may not use this file except in compliance  * with the License. You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing,  * software distributed under the License is distributed on an  * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  * KIND, either express or implied. See the License for the  * specific language governing permissions and limitations  * under the License.  */
end_comment

begin_package
package|package
name|sample
operator|.
name|ws
package|;
end_package

begin_import
import|import
name|org
operator|.
name|jboss
operator|.
name|jbossts
operator|.
name|XTSService
import|;
end_import

begin_import
import|import
name|org
operator|.
name|jboss
operator|.
name|jbossts
operator|.
name|txbridge
operator|.
name|inbound
operator|.
name|InboundBridgeRecoveryManager
import|;
end_import

begin_import
import|import
name|org
operator|.
name|jboss
operator|.
name|jbossts
operator|.
name|xts
operator|.
name|environment
operator|.
name|WSCEnvironmentBean
import|;
end_import

begin_import
import|import
name|org
operator|.
name|jboss
operator|.
name|jbossts
operator|.
name|xts
operator|.
name|environment
operator|.
name|XTSEnvironmentBean
import|;
end_import

begin_import
import|import
name|org
operator|.
name|jboss
operator|.
name|jbossts
operator|.
name|xts
operator|.
name|environment
operator|.
name|XTSPropertyManager
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
name|Value
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|context
operator|.
name|annotation
operator|.
name|Bean
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|context
operator|.
name|annotation
operator|.
name|Configuration
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|context
operator|.
name|annotation
operator|.
name|DependsOn
import|;
end_import

begin_class
annotation|@
name|Configuration
specifier|public
class|class
name|XTSConfig
block|{
annotation|@
name|Value
argument_list|(
literal|"${server.port}"
argument_list|)
specifier|private
name|int
name|port
decl_stmt|;
annotation|@
name|Bean
argument_list|(
name|initMethod
operator|=
literal|"start"
argument_list|,
name|destroyMethod
operator|=
literal|"stop"
argument_list|)
specifier|public
name|XTSService
name|xtsService
parameter_list|()
block|{
name|WSCEnvironmentBean
name|wscEnvironmentBean
init|=
name|XTSPropertyManager
operator|.
name|getWSCEnvironmentBean
argument_list|()
decl_stmt|;
name|wscEnvironmentBean
operator|.
name|setBindPort11
argument_list|(
name|port
argument_list|)
expr_stmt|;
name|XTSService
name|service
init|=
operator|new
name|XTSService
argument_list|()
decl_stmt|;
return|return
name|service
return|;
block|}
annotation|@
name|Bean
argument_list|(
name|initMethod
operator|=
literal|"start"
argument_list|,
name|destroyMethod
operator|=
literal|"stop"
argument_list|)
annotation|@
name|DependsOn
argument_list|(
block|{
literal|"xtsService"
block|}
argument_list|)
specifier|public
name|InboundBridgeRecoveryManager
name|inboundBridgeRecoveryManager
parameter_list|()
block|{
return|return
operator|new
name|InboundBridgeRecoveryManager
argument_list|()
return|;
block|}
block|}
end_class

end_unit

