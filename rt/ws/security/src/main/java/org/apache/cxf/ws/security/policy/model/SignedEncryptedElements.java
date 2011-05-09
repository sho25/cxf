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
name|Iterator
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
name|PolicyComponent
import|;
end_import

begin_class
specifier|public
class|class
name|SignedEncryptedElements
extends|extends
name|AbstractSecurityAssertion
block|{
specifier|private
name|List
argument_list|<
name|String
argument_list|>
name|xPathExpressions
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
name|String
argument_list|>
name|declaredNamespaces
init|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
argument_list|()
decl_stmt|;
specifier|private
name|String
name|xPathVersion
decl_stmt|;
comment|/**      * Just a flag to identify whether this holds sign element info or encr elements info      */
specifier|private
name|boolean
name|signedElements
decl_stmt|;
specifier|public
name|SignedEncryptedElements
parameter_list|(
name|boolean
name|signedElements
parameter_list|,
name|SPConstants
name|version
parameter_list|)
block|{
name|super
argument_list|(
name|version
argument_list|)
expr_stmt|;
name|this
operator|.
name|signedElements
operator|=
name|signedElements
expr_stmt|;
block|}
comment|/**      * @return Returns the xPathExpressions.      */
specifier|public
name|List
argument_list|<
name|String
argument_list|>
name|getXPathExpressions
parameter_list|()
block|{
return|return
name|xPathExpressions
return|;
block|}
specifier|public
name|void
name|addXPathExpression
parameter_list|(
name|String
name|expr
parameter_list|)
block|{
name|this
operator|.
name|xPathExpressions
operator|.
name|add
argument_list|(
name|expr
argument_list|)
expr_stmt|;
block|}
comment|/**      * @return Returns the xPathVersion.      */
specifier|public
name|String
name|getXPathVersion
parameter_list|()
block|{
return|return
name|xPathVersion
return|;
block|}
comment|/**      * @param pathVersion The xPathVersion to set.      */
specifier|public
name|void
name|setXPathVersion
parameter_list|(
name|String
name|pathVersion
parameter_list|)
block|{
name|xPathVersion
operator|=
name|pathVersion
expr_stmt|;
block|}
comment|/**      * @return Returns the signedElements.      */
specifier|public
name|boolean
name|isSignedElemets
parameter_list|()
block|{
return|return
name|signedElements
return|;
block|}
specifier|public
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|getDeclaredNamespaces
parameter_list|()
block|{
return|return
name|declaredNamespaces
return|;
block|}
specifier|public
name|void
name|addDeclaredNamespaces
parameter_list|(
name|String
name|uri
parameter_list|,
name|String
name|prefix
parameter_list|)
block|{
name|declaredNamespaces
operator|.
name|put
argument_list|(
name|prefix
argument_list|,
name|uri
argument_list|)
expr_stmt|;
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
comment|//<sp:SignedElements> |<sp:EncryptedElements>
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
comment|// xmlns:sp=".."
name|writer
operator|.
name|writeNamespace
argument_list|(
name|prefix
argument_list|,
name|namespaceURI
argument_list|)
expr_stmt|;
if|if
condition|(
name|xPathVersion
operator|!=
literal|null
condition|)
block|{
name|writer
operator|.
name|writeAttribute
argument_list|(
name|prefix
argument_list|,
name|namespaceURI
argument_list|,
name|SPConstants
operator|.
name|XPATH_VERSION
argument_list|,
name|xPathVersion
argument_list|)
expr_stmt|;
block|}
name|String
name|xpathExpression
decl_stmt|;
for|for
control|(
name|Iterator
argument_list|<
name|String
argument_list|>
name|iterator
init|=
name|xPathExpressions
operator|.
name|iterator
argument_list|()
init|;
name|iterator
operator|.
name|hasNext
argument_list|()
condition|;
control|)
block|{
name|xpathExpression
operator|=
name|iterator
operator|.
name|next
argument_list|()
expr_stmt|;
comment|//<sp:XPath ..>
name|writer
operator|.
name|writeStartElement
argument_list|(
name|prefix
argument_list|,
name|SPConstants
operator|.
name|XPATH_EXPR
argument_list|,
name|namespaceURI
argument_list|)
expr_stmt|;
name|writer
operator|.
name|writeCharacters
argument_list|(
name|xpathExpression
argument_list|)
expr_stmt|;
name|writer
operator|.
name|writeEndElement
argument_list|()
expr_stmt|;
block|}
comment|//</sp:SignedElements> |</sp:EncryptedElements>
name|writer
operator|.
name|writeEndElement
argument_list|()
expr_stmt|;
block|}
specifier|public
name|QName
name|getRealName
parameter_list|()
block|{
if|if
condition|(
name|signedElements
condition|)
block|{
return|return
name|constants
operator|.
name|getSignedElements
argument_list|()
return|;
block|}
return|return
name|constants
operator|.
name|getEncryptedElements
argument_list|()
return|;
block|}
specifier|public
name|QName
name|getName
parameter_list|()
block|{
if|if
condition|(
name|signedElements
condition|)
block|{
return|return
name|SP12Constants
operator|.
name|INSTANCE
operator|.
name|getSignedElements
argument_list|()
return|;
block|}
return|return
name|SP12Constants
operator|.
name|INSTANCE
operator|.
name|getEncryptedElements
argument_list|()
return|;
block|}
specifier|public
name|PolicyComponent
name|normalize
parameter_list|()
block|{
return|return
name|this
return|;
block|}
block|}
end_class

end_unit

