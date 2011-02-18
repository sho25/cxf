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
name|primitive
package|;
end_package

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
name|neethi
operator|.
name|Assertion
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|neethi
operator|.
name|builders
operator|.
name|xml
operator|.
name|XMLPrimitiveAssertionBuilder
import|;
end_import

begin_comment
comment|/**  *   */
end_comment

begin_class
specifier|public
class|class
name|PrimitiveAssertion
extends|extends
name|org
operator|.
name|apache
operator|.
name|neethi
operator|.
name|builders
operator|.
name|PrimitiveAssertion
block|{
specifier|public
name|PrimitiveAssertion
parameter_list|()
block|{
name|super
argument_list|()
expr_stmt|;
block|}
specifier|public
name|PrimitiveAssertion
parameter_list|(
name|QName
name|n
parameter_list|)
block|{
name|super
argument_list|(
name|n
argument_list|,
literal|false
argument_list|)
expr_stmt|;
block|}
specifier|public
name|PrimitiveAssertion
parameter_list|(
name|QName
name|n
parameter_list|,
name|boolean
name|o
parameter_list|)
block|{
name|super
argument_list|(
name|n
argument_list|,
name|o
argument_list|)
expr_stmt|;
block|}
specifier|public
name|PrimitiveAssertion
parameter_list|(
name|QName
name|n
parameter_list|,
name|boolean
name|o
parameter_list|,
name|boolean
name|i
parameter_list|)
block|{
name|super
argument_list|(
name|n
argument_list|,
name|o
argument_list|,
name|i
argument_list|)
expr_stmt|;
block|}
specifier|public
name|PrimitiveAssertion
parameter_list|(
name|QName
name|n
parameter_list|,
name|boolean
name|o
parameter_list|,
name|boolean
name|i
parameter_list|,
name|Map
argument_list|<
name|QName
argument_list|,
name|String
argument_list|>
name|atts
parameter_list|)
block|{
name|super
argument_list|(
name|n
argument_list|,
name|o
argument_list|,
name|i
argument_list|,
name|atts
argument_list|)
expr_stmt|;
block|}
specifier|public
name|PrimitiveAssertion
parameter_list|(
name|Element
name|element
parameter_list|)
block|{
name|super
argument_list|(
operator|new
name|QName
argument_list|(
name|element
operator|.
name|getNamespaceURI
argument_list|()
argument_list|,
name|element
operator|.
name|getLocalName
argument_list|()
argument_list|)
argument_list|,
name|XMLPrimitiveAssertionBuilder
operator|.
name|isOptional
argument_list|(
name|element
argument_list|)
argument_list|,
name|XMLPrimitiveAssertionBuilder
operator|.
name|isIgnorable
argument_list|(
name|element
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|protected
name|Assertion
name|clone
parameter_list|(
name|boolean
name|opt
parameter_list|)
block|{
return|return
operator|new
name|PrimitiveAssertion
argument_list|(
name|name
argument_list|,
name|opt
argument_list|,
name|ignorable
argument_list|)
return|;
block|}
block|}
end_class

end_unit

