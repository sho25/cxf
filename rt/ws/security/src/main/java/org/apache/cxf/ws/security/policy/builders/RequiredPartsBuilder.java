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
name|policy
operator|.
name|builders
package|;
end_package

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
name|w3c
operator|.
name|dom
operator|.
name|Node
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
name|security
operator|.
name|policy
operator|.
name|SP12Constants
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
name|security
operator|.
name|policy
operator|.
name|SPConstants
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
name|security
operator|.
name|policy
operator|.
name|model
operator|.
name|Header
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
name|security
operator|.
name|policy
operator|.
name|model
operator|.
name|RequiredParts
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
name|RequiredPartsBuilder
implements|implements
name|AssertionBuilder
argument_list|<
name|Element
argument_list|>
block|{
specifier|public
name|Assertion
name|build
parameter_list|(
name|Element
name|element
parameter_list|,
name|AssertionBuilderFactory
name|factory
parameter_list|)
throws|throws
name|IllegalArgumentException
block|{
name|RequiredParts
name|requiredParts
init|=
operator|new
name|RequiredParts
argument_list|(
name|SP12Constants
operator|.
name|INSTANCE
argument_list|)
decl_stmt|;
name|Node
name|nd
init|=
name|element
operator|.
name|getFirstChild
argument_list|()
decl_stmt|;
while|while
condition|(
name|nd
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
name|nd
operator|instanceof
name|Element
condition|)
block|{
name|processElement
argument_list|(
operator|(
name|Element
operator|)
name|nd
argument_list|,
name|requiredParts
argument_list|)
expr_stmt|;
block|}
name|nd
operator|=
name|nd
operator|.
name|getNextSibling
argument_list|()
expr_stmt|;
block|}
return|return
name|requiredParts
return|;
block|}
specifier|public
name|QName
index|[]
name|getKnownElements
parameter_list|()
block|{
return|return
operator|new
name|QName
index|[]
block|{
name|SP12Constants
operator|.
name|REQUIRED_PARTS
block|}
return|;
block|}
specifier|private
name|void
name|processElement
parameter_list|(
name|Element
name|element
parameter_list|,
name|RequiredParts
name|parent
parameter_list|)
block|{
if|if
condition|(
literal|"Header"
operator|.
name|equals
argument_list|(
name|element
operator|.
name|getLocalName
argument_list|()
argument_list|)
condition|)
block|{
name|String
name|nameAttribute
init|=
name|element
operator|.
name|getAttributeNS
argument_list|(
literal|null
argument_list|,
name|SPConstants
operator|.
name|NAME
argument_list|)
decl_stmt|;
if|if
condition|(
name|nameAttribute
operator|==
literal|null
condition|)
block|{
name|nameAttribute
operator|=
literal|""
expr_stmt|;
block|}
name|String
name|namespaceAttribute
init|=
name|element
operator|.
name|getAttributeNS
argument_list|(
literal|null
argument_list|,
name|SPConstants
operator|.
name|NAMESPACE
argument_list|)
decl_stmt|;
if|if
condition|(
literal|""
operator|.
name|equals
argument_list|(
name|namespaceAttribute
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"sp:RequiredParts/sp:Header@Namespace must have a value"
argument_list|)
throw|;
block|}
name|parent
operator|.
name|addHeader
argument_list|(
operator|new
name|Header
argument_list|(
name|nameAttribute
argument_list|,
name|namespaceAttribute
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

