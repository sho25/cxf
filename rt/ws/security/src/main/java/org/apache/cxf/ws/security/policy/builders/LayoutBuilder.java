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
name|apache
operator|.
name|cxf
operator|.
name|helpers
operator|.
name|DOMUtils
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
name|PolicyConstants
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
name|SP11Constants
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
name|Layout
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
name|LayoutBuilder
implements|implements
name|AssertionBuilder
argument_list|<
name|Element
argument_list|>
block|{
specifier|public
name|LayoutBuilder
parameter_list|()
block|{     }
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
name|SP11Constants
operator|.
name|LAYOUT
block|,
name|SP12Constants
operator|.
name|LAYOUT
block|}
return|;
block|}
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
name|SPConstants
name|consts
init|=
name|SP11Constants
operator|.
name|SP_NS
operator|.
name|equals
argument_list|(
name|element
operator|.
name|getNamespaceURI
argument_list|()
argument_list|)
condition|?
name|SP11Constants
operator|.
name|INSTANCE
else|:
name|SP12Constants
operator|.
name|INSTANCE
decl_stmt|;
name|Layout
name|layout
init|=
operator|new
name|Layout
argument_list|(
name|consts
argument_list|)
decl_stmt|;
name|processAlternative
argument_list|(
name|element
argument_list|,
name|layout
argument_list|,
name|consts
argument_list|)
expr_stmt|;
return|return
name|layout
return|;
block|}
specifier|public
name|void
name|processAlternative
parameter_list|(
name|Element
name|element
parameter_list|,
name|Layout
name|parent
parameter_list|,
name|SPConstants
name|consts
parameter_list|)
block|{
name|Element
name|polEl
init|=
name|PolicyConstants
operator|.
name|findPolicyElement
argument_list|(
name|element
argument_list|)
decl_stmt|;
if|if
condition|(
name|polEl
operator|==
literal|null
operator|&&
name|consts
operator|!=
name|SP11Constants
operator|.
name|INSTANCE
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"sp:Layout/wsp:Policy must have a value"
argument_list|)
throw|;
block|}
if|if
condition|(
name|polEl
operator|!=
literal|null
condition|)
block|{
name|Element
name|child
init|=
name|DOMUtils
operator|.
name|getFirstElement
argument_list|(
name|polEl
argument_list|)
decl_stmt|;
if|if
condition|(
name|child
operator|!=
literal|null
condition|)
block|{
name|parent
operator|.
name|setValue
argument_list|(
name|SPConstants
operator|.
name|Layout
operator|.
name|valueOf
argument_list|(
name|child
operator|.
name|getLocalName
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
end_class

end_unit

