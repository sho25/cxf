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
name|systest
operator|.
name|ws
operator|.
name|addressing
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
name|HashMap
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
name|Map
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
name|message
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
name|ContextUtils
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
name|Names
import|;
end_import

begin_import
import|import static
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
operator|.
name|ADDRESSING_PROPERTIES_INBOUND
import|;
end_import

begin_import
import|import static
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
operator|.
name|ADDRESSING_PROPERTIES_OUTBOUND
import|;
end_import

begin_comment
comment|/**  * Verifies presence of MAPs in the context.  */
end_comment

begin_class
specifier|public
class|class
name|MAPVerifier
extends|extends
name|AbstractPhaseInterceptor
argument_list|<
name|Message
argument_list|>
block|{
name|VerificationCache
name|verificationCache
decl_stmt|;
name|List
argument_list|<
name|String
argument_list|>
name|expectedExposedAs
init|=
operator|new
name|ArrayList
argument_list|<
name|String
argument_list|>
argument_list|()
decl_stmt|;
specifier|private
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|mapProperties
decl_stmt|;
specifier|public
name|MAPVerifier
parameter_list|()
block|{
name|super
argument_list|(
name|Phase
operator|.
name|POST_LOGICAL
argument_list|)
expr_stmt|;
name|mapProperties
operator|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
argument_list|()
expr_stmt|;
name|mapProperties
operator|.
name|put
argument_list|(
name|MAPTest
operator|.
name|INBOUND_KEY
argument_list|,
name|ADDRESSING_PROPERTIES_INBOUND
argument_list|)
expr_stmt|;
name|mapProperties
operator|.
name|put
argument_list|(
name|MAPTest
operator|.
name|OUTBOUND_KEY
argument_list|,
name|ADDRESSING_PROPERTIES_OUTBOUND
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|handleMessage
parameter_list|(
name|Message
name|message
parameter_list|)
block|{
name|verify
argument_list|(
name|message
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|handleFault
parameter_list|(
name|Message
name|message
parameter_list|)
block|{
name|verify
argument_list|(
name|message
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|verify
parameter_list|(
name|Message
name|message
parameter_list|)
block|{
name|boolean
name|isOutbound
init|=
name|ContextUtils
operator|.
name|isOutbound
argument_list|(
name|message
argument_list|)
decl_stmt|;
name|String
name|mapProperty
init|=
operator|(
name|String
operator|)
name|mapProperties
operator|.
name|get
argument_list|(
name|isOutbound
condition|?
name|MAPTest
operator|.
name|OUTBOUND_KEY
else|:
name|MAPTest
operator|.
name|INBOUND_KEY
argument_list|)
decl_stmt|;
name|AddressingProperties
name|maps
init|=
operator|(
name|AddressingProperties
operator|)
name|message
operator|.
name|get
argument_list|(
name|mapProperty
argument_list|)
decl_stmt|;
if|if
condition|(
name|maps
operator|==
literal|null
condition|)
block|{
return|return;
block|}
if|if
condition|(
name|ContextUtils
operator|.
name|isRequestor
argument_list|(
name|message
argument_list|)
condition|)
block|{
if|if
condition|(
name|isOutbound
condition|)
block|{
name|String
name|exposeAs
init|=
name|getExpectedExposeAs
argument_list|(
literal|false
argument_list|)
decl_stmt|;
if|if
condition|(
name|exposeAs
operator|!=
literal|null
condition|)
block|{
name|maps
operator|.
name|exposeAs
argument_list|(
name|exposeAs
argument_list|)
expr_stmt|;
block|}
block|}
else|else
block|{
name|String
name|exposeAs
init|=
name|getExpectedExposeAs
argument_list|(
literal|true
argument_list|)
decl_stmt|;
name|String
name|expected
init|=
name|exposeAs
operator|!=
literal|null
condition|?
name|exposeAs
else|:
name|Names
operator|.
name|WSA_NAMESPACE_NAME
decl_stmt|;
if|if
condition|(
name|maps
operator|.
name|getNamespaceURI
argument_list|()
operator|!=
name|expected
condition|)
block|{
name|verificationCache
operator|.
name|put
argument_list|(
literal|"Incoming version mismatch"
operator|+
literal|" expected: "
operator|+
name|expected
operator|+
literal|" got: "
operator|+
name|maps
operator|.
name|getNamespaceURI
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|exposeAs
operator|=
literal|null
expr_stmt|;
block|}
block|}
name|verificationCache
operator|.
name|put
argument_list|(
name|MAPTest
operator|.
name|verifyMAPs
argument_list|(
name|maps
argument_list|,
name|this
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|private
name|String
name|getExpectedExposeAs
parameter_list|(
name|boolean
name|remove
parameter_list|)
block|{
name|int
name|size
init|=
name|expectedExposedAs
operator|.
name|size
argument_list|()
decl_stmt|;
return|return
name|size
operator|==
literal|0
condition|?
literal|null
else|:
name|remove
condition|?
name|expectedExposedAs
operator|.
name|remove
argument_list|(
name|size
operator|-
literal|1
argument_list|)
else|:
name|expectedExposedAs
operator|.
name|get
argument_list|(
name|size
operator|-
literal|1
argument_list|)
return|;
block|}
specifier|public
name|void
name|setVerificationCache
parameter_list|(
name|VerificationCache
name|cache
parameter_list|)
block|{
name|verificationCache
operator|=
name|cache
expr_stmt|;
block|}
specifier|public
name|void
name|addToExpectedExposedAs
parameter_list|(
name|String
name|str
parameter_list|)
block|{
name|expectedExposedAs
operator|.
name|add
argument_list|(
name|str
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

