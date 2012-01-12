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
name|sts
operator|.
name|interceptor
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
name|binding
operator|.
name|soap
operator|.
name|SoapMessage
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
name|interceptor
operator|.
name|Fault
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
name|phase
operator|.
name|AbstractPhaseInterceptor
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
name|phase
operator|.
name|Phase
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
name|ws
operator|.
name|addressing
operator|.
name|AddressingProperties
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
name|ws
operator|.
name|addressing
operator|.
name|AttributedURIType
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
name|ws
operator|.
name|addressing
operator|.
name|JAXWSAConstants
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
name|ws
operator|.
name|addressing
operator|.
name|impl
operator|.
name|AddressingPropertiesImpl
import|;
end_import

begin_class
specifier|public
class|class
name|SCTOutInterceptor
extends|extends
name|AbstractPhaseInterceptor
argument_list|<
name|SoapMessage
argument_list|>
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
name|SCTOutInterceptor
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|public
name|SCTOutInterceptor
parameter_list|()
block|{
name|super
argument_list|(
name|Phase
operator|.
name|PRE_PROTOCOL
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|handleMessage
parameter_list|(
name|SoapMessage
name|message
parameter_list|)
throws|throws
name|Fault
block|{
name|AddressingProperties
name|inProps
init|=
operator|(
name|AddressingProperties
operator|)
name|message
operator|.
name|getContextualProperty
argument_list|(
name|JAXWSAConstants
operator|.
name|SERVER_ADDRESSING_PROPERTIES_INBOUND
argument_list|)
decl_stmt|;
name|AddressingProperties
name|outProps
init|=
operator|(
name|AddressingProperties
operator|)
name|message
operator|.
name|getContextualProperty
argument_list|(
name|JAXWSAConstants
operator|.
name|SERVER_ADDRESSING_PROPERTIES_OUTBOUND
argument_list|)
decl_stmt|;
if|if
condition|(
name|inProps
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
name|outProps
operator|==
literal|null
condition|)
block|{
name|outProps
operator|=
operator|new
name|AddressingPropertiesImpl
argument_list|(
name|inProps
operator|.
name|getNamespaceURI
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|AttributedURIType
name|action
init|=
operator|new
name|AttributedURIType
argument_list|()
decl_stmt|;
name|action
operator|.
name|setValue
argument_list|(
name|inProps
operator|.
name|getAction
argument_list|()
operator|.
name|getValue
argument_list|()
operator|.
name|replace
argument_list|(
literal|"/RST/"
argument_list|,
literal|"/RSTR/"
argument_list|)
argument_list|)
expr_stmt|;
name|outProps
operator|.
name|setAction
argument_list|(
name|action
argument_list|)
expr_stmt|;
name|message
operator|.
name|put
argument_list|(
name|JAXWSAConstants
operator|.
name|SERVER_ADDRESSING_PROPERTIES_OUTBOUND
argument_list|,
name|outProps
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

