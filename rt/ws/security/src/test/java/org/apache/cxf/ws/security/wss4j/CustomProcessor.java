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
name|ws
operator|.
name|security
operator|.
name|wss4j
package|;
end_package

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|wss4j
operator|.
name|common
operator|.
name|ext
operator|.
name|WSSecurityException
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|wss4j
operator|.
name|dom
operator|.
name|WSConstants
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|wss4j
operator|.
name|dom
operator|.
name|engine
operator|.
name|WSSecurityEngineResult
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|wss4j
operator|.
name|dom
operator|.
name|handler
operator|.
name|RequestData
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|wss4j
operator|.
name|dom
operator|.
name|processor
operator|.
name|Processor
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|wss4j
operator|.
name|dom
operator|.
name|validate
operator|.
name|Validator
import|;
end_import

begin_comment
comment|/**  * a custom processor that inserts itself into the results vector  */
end_comment

begin_class
specifier|public
class|class
name|CustomProcessor
implements|implements
name|Processor
block|{
specifier|public
specifier|final
name|java
operator|.
name|util
operator|.
name|List
argument_list|<
name|WSSecurityEngineResult
argument_list|>
name|handleToken
parameter_list|(
specifier|final
name|org
operator|.
name|w3c
operator|.
name|dom
operator|.
name|Element
name|elem
parameter_list|,
specifier|final
name|RequestData
name|data
parameter_list|)
throws|throws
name|WSSecurityException
block|{
specifier|final
name|WSSecurityEngineResult
name|result
init|=
operator|new
name|WSSecurityEngineResult
argument_list|(
name|WSConstants
operator|.
name|SIGN
argument_list|)
decl_stmt|;
name|result
operator|.
name|put
argument_list|(
literal|"foo"
argument_list|,
name|this
argument_list|)
expr_stmt|;
name|data
operator|.
name|getWsDocInfo
argument_list|()
operator|.
name|addResult
argument_list|(
name|result
argument_list|)
expr_stmt|;
return|return
name|java
operator|.
name|util
operator|.
name|Collections
operator|.
name|singletonList
argument_list|(
name|result
argument_list|)
return|;
block|}
specifier|public
name|void
name|setValidator
parameter_list|(
name|Validator
name|validator
parameter_list|)
block|{
comment|//
block|}
block|}
end_class

end_unit

