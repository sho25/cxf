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

begin_class
specifier|public
class|class
name|ProtectionToken
extends|extends
name|TokenWrapper
block|{
specifier|public
name|ProtectionToken
parameter_list|(
name|PolicyBuilder
name|b
parameter_list|)
block|{
name|super
argument_list|(
name|SP12Constants
operator|.
name|INSTANCE
argument_list|,
name|b
argument_list|)
expr_stmt|;
block|}
specifier|public
name|ProtectionToken
parameter_list|(
name|SPConstants
name|version
parameter_list|,
name|PolicyBuilder
name|b
parameter_list|)
block|{
name|super
argument_list|(
name|version
argument_list|,
name|b
argument_list|)
expr_stmt|;
block|}
comment|/**      * @return Returns the protectionToken.      */
specifier|public
name|Token
name|getProtectionToken
parameter_list|()
block|{
return|return
name|getToken
argument_list|()
return|;
block|}
comment|/**      * @param protectionToken The protectionToken to set.      */
specifier|public
name|void
name|setProtectionToken
parameter_list|(
name|Token
name|protectionToken
parameter_list|)
block|{
name|setToken
argument_list|(
name|protectionToken
argument_list|)
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
name|getProtectionToken
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
name|getProtectionToken
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
name|localname
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
decl_stmt|;
name|String
name|writerPrefix
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
name|writerPrefix
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
else|else
block|{
name|prefix
operator|=
name|writerPrefix
expr_stmt|;
block|}
comment|//<sp:ProtectionToken>
name|writer
operator|.
name|writeStartElement
argument_list|(
name|prefix
argument_list|,
name|localname
argument_list|,
name|namespaceURI
argument_list|)
expr_stmt|;
if|if
condition|(
name|writerPrefix
operator|==
literal|null
condition|)
block|{
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
block|}
name|String
name|policyLocalName
init|=
name|SPConstants
operator|.
name|POLICY
operator|.
name|getLocalPart
argument_list|()
decl_stmt|;
name|String
name|policyNamespaceURI
init|=
name|SPConstants
operator|.
name|POLICY
operator|.
name|getNamespaceURI
argument_list|()
decl_stmt|;
name|String
name|wspPrefix
decl_stmt|;
name|String
name|wspWriterPrefix
init|=
name|writer
operator|.
name|getPrefix
argument_list|(
name|policyNamespaceURI
argument_list|)
decl_stmt|;
if|if
condition|(
name|wspWriterPrefix
operator|==
literal|null
condition|)
block|{
name|wspPrefix
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
name|wspPrefix
argument_list|,
name|policyNamespaceURI
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|wspPrefix
operator|=
name|wspWriterPrefix
expr_stmt|;
block|}
comment|//<wsp:Policy>
name|writer
operator|.
name|writeStartElement
argument_list|(
name|wspPrefix
argument_list|,
name|policyLocalName
argument_list|,
name|policyNamespaceURI
argument_list|)
expr_stmt|;
if|if
condition|(
name|wspWriterPrefix
operator|==
literal|null
condition|)
block|{
comment|// xmlns:wsp=".."
name|writer
operator|.
name|writeNamespace
argument_list|(
name|wspPrefix
argument_list|,
name|policyNamespaceURI
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|token
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|RuntimeException
argument_list|(
literal|"ProtectionToken is not set"
argument_list|)
throw|;
block|}
name|token
operator|.
name|serialize
argument_list|(
name|writer
argument_list|)
expr_stmt|;
comment|//</wsp:Policy>
name|writer
operator|.
name|writeEndElement
argument_list|()
expr_stmt|;
comment|//</sp:ProtectionToken>
name|writer
operator|.
name|writeEndElement
argument_list|()
expr_stmt|;
block|}
block|}
end_class

end_unit

