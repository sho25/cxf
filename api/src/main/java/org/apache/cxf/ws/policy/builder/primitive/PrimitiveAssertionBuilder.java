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
name|Bus
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
name|AssertionBuilderFactory
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
name|AssertionBuilder
import|;
end_import

begin_class
specifier|public
class|class
name|PrimitiveAssertionBuilder
implements|implements
name|AssertionBuilder
argument_list|<
name|Element
argument_list|>
block|{
specifier|protected
name|Bus
name|bus
decl_stmt|;
specifier|private
name|QName
name|knownElements
index|[]
init|=
block|{}
decl_stmt|;
specifier|public
name|PrimitiveAssertionBuilder
parameter_list|()
block|{     }
specifier|public
name|PrimitiveAssertionBuilder
parameter_list|(
name|Collection
argument_list|<
name|QName
argument_list|>
name|els
parameter_list|)
block|{
name|knownElements
operator|=
name|els
operator|.
name|toArray
argument_list|(
operator|new
name|QName
index|[
name|els
operator|.
name|size
argument_list|()
index|]
argument_list|)
expr_stmt|;
block|}
specifier|public
name|PrimitiveAssertionBuilder
parameter_list|(
name|QName
name|els
index|[]
parameter_list|)
block|{
name|knownElements
operator|=
name|els
expr_stmt|;
block|}
specifier|public
name|void
name|setBus
parameter_list|(
name|Bus
name|b
parameter_list|)
block|{
name|bus
operator|=
name|b
expr_stmt|;
block|}
specifier|public
name|Assertion
name|build
parameter_list|(
name|Element
name|element
parameter_list|,
name|AssertionBuilderFactory
name|fact
parameter_list|)
block|{
return|return
operator|new
name|PrimitiveAssertion
argument_list|(
name|element
argument_list|)
return|;
block|}
specifier|public
name|QName
index|[]
name|getKnownElements
parameter_list|()
block|{
return|return
name|knownElements
return|;
block|}
specifier|public
name|void
name|setKnownElements
parameter_list|(
name|Collection
argument_list|<
name|QName
argument_list|>
name|k
parameter_list|)
block|{
name|knownElements
operator|=
name|k
operator|.
name|toArray
argument_list|(
operator|new
name|QName
index|[
name|k
operator|.
name|size
argument_list|()
index|]
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|setKnownElements
parameter_list|(
name|QName
name|k
index|[]
parameter_list|)
block|{
name|knownElements
operator|=
name|k
expr_stmt|;
block|}
block|}
end_class

end_unit

