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
name|policy
operator|.
name|builder
operator|.
name|xml
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Collection
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|namespace
operator|.
name|QName
import|;
end_import

begin_import
import|import
name|org
operator|.
name|w3c
operator|.
name|dom
operator|.
name|Element
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
name|policy
operator|.
name|PolicyAssertion
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
name|policy
operator|.
name|builder
operator|.
name|primitive
operator|.
name|PrimitiveAssertionBuilder
import|;
end_import

begin_class
specifier|public
class|class
name|XMLPrimitiveAssertionBuilder
extends|extends
name|PrimitiveAssertionBuilder
block|{
specifier|public
name|XMLPrimitiveAssertionBuilder
parameter_list|()
block|{
name|super
argument_list|()
expr_stmt|;
block|}
specifier|public
name|XMLPrimitiveAssertionBuilder
parameter_list|(
name|Collection
argument_list|<
name|QName
argument_list|>
name|els
parameter_list|)
block|{
name|super
argument_list|(
name|els
argument_list|)
expr_stmt|;
block|}
specifier|public
name|PolicyAssertion
name|build
parameter_list|(
name|Element
name|element
parameter_list|)
block|{
return|return
operator|new
name|XmlPrimitiveAssertion
argument_list|(
name|element
argument_list|,
name|getPolicyConstants
argument_list|()
argument_list|)
return|;
block|}
block|}
end_class

end_unit

