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
name|PolicyBuilder
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
name|AlgorithmSuite
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
name|SupportingToken
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
name|TransportBinding
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
name|TransportToken
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
name|Constants
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
name|TransportBindingBuilder
implements|implements
name|AssertionBuilder
argument_list|<
name|Element
argument_list|>
block|{
specifier|private
specifier|static
specifier|final
name|QName
name|KNOWN_ELEMENTS
index|[]
init|=
block|{
name|SP11Constants
operator|.
name|TRANSPORT_BINDING
block|,
name|SP12Constants
operator|.
name|TRANSPORT_BINDING
block|}
decl_stmt|;
name|PolicyBuilder
name|builder
decl_stmt|;
specifier|public
name|TransportBindingBuilder
parameter_list|(
name|PolicyBuilder
name|b
parameter_list|)
block|{
name|builder
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
name|TransportBinding
name|transportBinding
init|=
operator|new
name|TransportBinding
argument_list|(
name|consts
argument_list|,
name|builder
argument_list|)
decl_stmt|;
name|processAlternative
argument_list|(
name|element
argument_list|,
name|transportBinding
argument_list|,
name|consts
argument_list|,
name|factory
argument_list|)
expr_stmt|;
return|return
name|transportBinding
return|;
block|}
specifier|public
name|QName
index|[]
name|getKnownElements
parameter_list|()
block|{
return|return
name|KNOWN_ELEMENTS
return|;
block|}
specifier|private
name|void
name|processAlternative
parameter_list|(
name|Element
name|element
parameter_list|,
name|TransportBinding
name|parent
parameter_list|,
name|SPConstants
name|consts
parameter_list|,
name|AssertionBuilderFactory
name|factory
parameter_list|)
block|{
name|Element
name|polEl
init|=
name|DOMUtils
operator|.
name|getFirstElement
argument_list|(
name|element
argument_list|)
decl_stmt|;
while|while
condition|(
name|polEl
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
name|Constants
operator|.
name|isPolicyElement
argument_list|(
operator|new
name|QName
argument_list|(
name|polEl
operator|.
name|getNamespaceURI
argument_list|()
argument_list|,
name|polEl
operator|.
name|getLocalName
argument_list|()
argument_list|)
argument_list|)
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
while|while
condition|(
name|child
operator|!=
literal|null
condition|)
block|{
name|String
name|name
init|=
name|child
operator|.
name|getLocalName
argument_list|()
decl_stmt|;
if|if
condition|(
name|name
operator|.
name|equals
argument_list|(
name|SPConstants
operator|.
name|ALGO_SUITE
argument_list|)
condition|)
block|{
name|parent
operator|.
name|setAlgorithmSuite
argument_list|(
operator|(
name|AlgorithmSuite
operator|)
operator|new
name|AlgorithmSuiteBuilder
argument_list|()
operator|.
name|build
argument_list|(
name|child
argument_list|,
name|factory
argument_list|)
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|name
operator|.
name|equals
argument_list|(
name|SPConstants
operator|.
name|TRANSPORT_TOKEN
argument_list|)
condition|)
block|{
name|parent
operator|.
name|setTransportToken
argument_list|(
operator|(
name|TransportToken
operator|)
operator|new
name|TransportTokenBuilder
argument_list|(
name|builder
argument_list|)
operator|.
name|build
argument_list|(
name|child
argument_list|,
name|factory
argument_list|)
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|name
operator|.
name|equals
argument_list|(
name|SPConstants
operator|.
name|INCLUDE_TIMESTAMP
argument_list|)
condition|)
block|{
name|parent
operator|.
name|setIncludeTimestamp
argument_list|(
literal|true
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|name
operator|.
name|equals
argument_list|(
name|SPConstants
operator|.
name|LAYOUT
argument_list|)
condition|)
block|{
name|parent
operator|.
name|setLayout
argument_list|(
operator|(
name|Layout
operator|)
operator|new
name|LayoutBuilder
argument_list|()
operator|.
name|build
argument_list|(
name|child
argument_list|,
name|factory
argument_list|)
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|name
operator|.
name|equals
argument_list|(
name|SPConstants
operator|.
name|PROTECT_TOKENS
argument_list|)
condition|)
block|{
name|parent
operator|.
name|setTokenProtection
argument_list|(
literal|true
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|name
operator|.
name|equals
argument_list|(
name|SPConstants
operator|.
name|SIGNED_SUPPORTING_TOKENS
argument_list|)
operator|||
name|name
operator|.
name|equals
argument_list|(
name|SPConstants
operator|.
name|SIGNED_ENDORSING_SUPPORTING_TOKENS
argument_list|)
condition|)
block|{
if|if
condition|(
name|consts
operator|.
name|getVersion
argument_list|()
operator|==
name|SPConstants
operator|.
name|Version
operator|.
name|SP_V11
condition|)
block|{
name|parent
operator|.
name|setSignedSupportingToken
argument_list|(
operator|(
name|SupportingToken
operator|)
operator|new
name|SupportingTokensBuilder
argument_list|(
name|builder
argument_list|)
operator|.
name|build
argument_list|(
name|child
argument_list|,
name|factory
argument_list|)
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|parent
operator|.
name|setSignedSupportingToken
argument_list|(
operator|(
name|SupportingToken
operator|)
operator|new
name|SupportingTokens12Builder
argument_list|(
name|builder
argument_list|)
operator|.
name|build
argument_list|(
name|child
argument_list|,
name|factory
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
name|child
operator|=
name|DOMUtils
operator|.
name|getNextElement
argument_list|(
name|child
argument_list|)
expr_stmt|;
block|}
block|}
name|polEl
operator|=
name|DOMUtils
operator|.
name|getNextElement
argument_list|(
name|polEl
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

