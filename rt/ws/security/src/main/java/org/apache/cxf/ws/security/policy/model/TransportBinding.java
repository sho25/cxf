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
name|model
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
name|javax
operator|.
name|xml
operator|.
name|stream
operator|.
name|XMLStreamException
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|stream
operator|.
name|XMLStreamWriter
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
name|PrimitiveAssertion
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
name|neethi
operator|.
name|All
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
name|ExactlyOne
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
name|Policy
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
name|PolicyComponent
import|;
end_import

begin_class
specifier|public
class|class
name|TransportBinding
extends|extends
name|Binding
block|{
specifier|private
name|TransportToken
name|transportToken
decl_stmt|;
specifier|public
name|TransportBinding
parameter_list|(
name|SPConstants
name|version
parameter_list|)
block|{
name|super
argument_list|(
name|version
argument_list|)
expr_stmt|;
block|}
comment|/**      * @return Returns the transportToken.      */
specifier|public
name|TransportToken
name|getTransportToken
parameter_list|()
block|{
return|return
name|transportToken
return|;
block|}
comment|/**      * @param transportToken The transportToken to set.      */
specifier|public
name|void
name|setTransportToken
parameter_list|(
name|TransportToken
name|transportToken
parameter_list|)
block|{
name|this
operator|.
name|transportToken
operator|=
name|transportToken
expr_stmt|;
block|}
specifier|public
name|QName
name|getRealName
parameter_list|()
block|{
return|return
name|constants
operator|.
name|getTransportBinding
argument_list|()
return|;
block|}
specifier|public
name|QName
name|getName
parameter_list|()
block|{
return|return
name|SP12Constants
operator|.
name|INSTANCE
operator|.
name|getTransportBinding
argument_list|()
return|;
block|}
specifier|public
name|void
name|serialize
parameter_list|(
name|XMLStreamWriter
name|writer
parameter_list|)
throws|throws
name|XMLStreamException
block|{
name|String
name|localName
init|=
name|getRealName
argument_list|()
operator|.
name|getLocalPart
argument_list|()
decl_stmt|;
name|String
name|namespaceURI
init|=
name|getRealName
argument_list|()
operator|.
name|getNamespaceURI
argument_list|()
decl_stmt|;
name|String
name|prefix
init|=
name|writer
operator|.
name|getPrefix
argument_list|(
name|namespaceURI
argument_list|)
decl_stmt|;
if|if
condition|(
name|prefix
operator|==
literal|null
condition|)
block|{
name|prefix
operator|=
name|getRealName
argument_list|()
operator|.
name|getPrefix
argument_list|()
expr_stmt|;
name|writer
operator|.
name|setPrefix
argument_list|(
name|prefix
argument_list|,
name|namespaceURI
argument_list|)
expr_stmt|;
block|}
comment|//<sp:TransportBinding>
name|writer
operator|.
name|writeStartElement
argument_list|(
name|prefix
argument_list|,
name|localName
argument_list|,
name|namespaceURI
argument_list|)
expr_stmt|;
name|writer
operator|.
name|writeNamespace
argument_list|(
name|prefix
argument_list|,
name|namespaceURI
argument_list|)
expr_stmt|;
name|String
name|pPrefix
init|=
name|writer
operator|.
name|getPrefix
argument_list|(
name|SPConstants
operator|.
name|POLICY
operator|.
name|getNamespaceURI
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|pPrefix
operator|==
literal|null
condition|)
block|{
name|pPrefix
operator|=
name|SPConstants
operator|.
name|POLICY
operator|.
name|getPrefix
argument_list|()
expr_stmt|;
name|writer
operator|.
name|setPrefix
argument_list|(
name|pPrefix
argument_list|,
name|SPConstants
operator|.
name|POLICY
operator|.
name|getNamespaceURI
argument_list|()
argument_list|)
expr_stmt|;
block|}
comment|//<wsp:Policy>
name|writer
operator|.
name|writeStartElement
argument_list|(
name|pPrefix
argument_list|,
name|SPConstants
operator|.
name|POLICY
operator|.
name|getLocalPart
argument_list|()
argument_list|,
name|SPConstants
operator|.
name|POLICY
operator|.
name|getNamespaceURI
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
name|transportToken
operator|==
literal|null
condition|)
block|{
comment|// TODO more meaningful exception
throw|throw
operator|new
name|RuntimeException
argument_list|(
literal|"no TransportToken found"
argument_list|)
throw|;
block|}
comment|//<sp:TransportToken>
name|transportToken
operator|.
name|serialize
argument_list|(
name|writer
argument_list|)
expr_stmt|;
comment|//</sp:TransportToken>
name|AlgorithmSuite
name|algorithmSuite
init|=
name|getAlgorithmSuite
argument_list|()
decl_stmt|;
if|if
condition|(
name|algorithmSuite
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|RuntimeException
argument_list|(
literal|"no AlgorithmSuite found"
argument_list|)
throw|;
block|}
comment|//<sp:AlgorithmSuite>
name|algorithmSuite
operator|.
name|serialize
argument_list|(
name|writer
argument_list|)
expr_stmt|;
comment|//</sp:AlgorithmSuite>
name|Layout
name|layout
init|=
name|getLayout
argument_list|()
decl_stmt|;
if|if
condition|(
name|layout
operator|!=
literal|null
condition|)
block|{
comment|//<sp:Layout>
name|layout
operator|.
name|serialize
argument_list|(
name|writer
argument_list|)
expr_stmt|;
comment|//</sp:Layout>
block|}
if|if
condition|(
name|isIncludeTimestamp
argument_list|()
condition|)
block|{
comment|//<sp:IncludeTimestamp>
name|writer
operator|.
name|writeStartElement
argument_list|(
name|prefix
argument_list|,
name|SPConstants
operator|.
name|INCLUDE_TIMESTAMP
argument_list|,
name|namespaceURI
argument_list|)
expr_stmt|;
name|writer
operator|.
name|writeEndElement
argument_list|()
expr_stmt|;
comment|//</sp:IncludeTimestamp>
block|}
comment|//</wsp:Policy>
name|writer
operator|.
name|writeEndElement
argument_list|()
expr_stmt|;
comment|//</sp:TransportBinding>
name|writer
operator|.
name|writeEndElement
argument_list|()
expr_stmt|;
block|}
specifier|public
name|PolicyComponent
name|normalize
parameter_list|()
block|{
name|Policy
name|p
init|=
operator|new
name|Policy
argument_list|()
decl_stmt|;
name|ExactlyOne
name|ea
init|=
operator|new
name|ExactlyOne
argument_list|()
decl_stmt|;
name|p
operator|.
name|addPolicyComponent
argument_list|(
name|ea
argument_list|)
expr_stmt|;
name|All
name|all
init|=
operator|new
name|All
argument_list|()
decl_stmt|;
name|ea
operator|.
name|addPolicyComponent
argument_list|(
name|all
argument_list|)
expr_stmt|;
if|if
condition|(
name|transportToken
operator|!=
literal|null
condition|)
block|{
name|all
operator|.
name|addPolicyComponent
argument_list|(
name|transportToken
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|isIncludeTimestamp
argument_list|()
condition|)
block|{
name|all
operator|.
name|addPolicyComponent
argument_list|(
operator|new
name|PrimitiveAssertion
argument_list|(
name|SP12Constants
operator|.
name|INCLUDE_TIMESTAMP
argument_list|)
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|getLayout
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|all
operator|.
name|addPolicyComponent
argument_list|(
name|getLayout
argument_list|()
argument_list|)
expr_stmt|;
block|}
return|return
name|p
operator|.
name|normalize
argument_list|(
literal|true
argument_list|)
return|;
block|}
block|}
end_class

end_unit

