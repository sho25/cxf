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
name|security
operator|.
name|policy
operator|.
name|SPConstants
import|;
end_import

begin_comment
comment|/**  * @author Ruchith Fernando (ruchith.fernando@gmail.com)  */
end_comment

begin_class
specifier|public
class|class
name|HttpsToken
extends|extends
name|Token
block|{
specifier|private
name|boolean
name|requireClientCertificate
decl_stmt|;
specifier|private
name|boolean
name|httpBasicAuthentication
decl_stmt|;
specifier|private
name|boolean
name|httpDigestAuthentication
decl_stmt|;
specifier|public
name|HttpsToken
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
specifier|public
name|boolean
name|isRequireClientCertificate
parameter_list|()
block|{
return|return
name|requireClientCertificate
return|;
block|}
specifier|public
name|void
name|setRequireClientCertificate
parameter_list|(
name|boolean
name|requireClientCertificate
parameter_list|)
block|{
name|this
operator|.
name|requireClientCertificate
operator|=
name|requireClientCertificate
expr_stmt|;
block|}
comment|/**      * @return the httpBasicAuthentication      */
specifier|public
name|boolean
name|isHttpBasicAuthentication
parameter_list|()
block|{
return|return
name|httpBasicAuthentication
return|;
block|}
comment|/**      * @param httpBasicAuthentication the httpBasicAuthentication to set      */
specifier|public
name|void
name|setHttpBasicAuthentication
parameter_list|(
name|boolean
name|httpBasicAuthentication
parameter_list|)
block|{
name|this
operator|.
name|httpBasicAuthentication
operator|=
name|httpBasicAuthentication
expr_stmt|;
block|}
comment|/**      * @return the httpDigestAuthentication      */
specifier|public
name|boolean
name|isHttpDigestAuthentication
parameter_list|()
block|{
return|return
name|httpDigestAuthentication
return|;
block|}
comment|/**      * @param httpDigestAuthentication the httpDigestAuthentication to set      */
specifier|public
name|void
name|setHttpDigestAuthentication
parameter_list|(
name|boolean
name|httpDigestAuthentication
parameter_list|)
block|{
name|this
operator|.
name|httpDigestAuthentication
operator|=
name|httpDigestAuthentication
expr_stmt|;
block|}
specifier|public
name|QName
name|getName
parameter_list|()
block|{
return|return
name|constants
operator|.
name|getHttpsToken
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
name|getName
argument_list|()
operator|.
name|getLocalPart
argument_list|()
decl_stmt|;
name|String
name|namespaceURI
init|=
name|getName
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
name|getName
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
comment|//<sp:HttpsToken
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
name|constants
operator|.
name|getVersion
argument_list|()
operator|==
name|SPConstants
operator|.
name|Version
operator|.
name|SP_V12
condition|)
block|{
if|if
condition|(
name|isRequireClientCertificate
argument_list|()
operator|||
name|isHttpBasicAuthentication
argument_list|()
operator|||
name|isHttpDigestAuthentication
argument_list|()
condition|)
block|{
comment|//<wsp:Policy>
name|writer
operator|.
name|writeStartElement
argument_list|(
name|SPConstants
operator|.
name|POLICY
operator|.
name|getPrefix
argument_list|()
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
comment|/*                  * The ws policy 1.2 specification states that only one of those should be present, although a                  * web server (say tomcat) could be normally configured to require both a client certificate                  * and a http user/pwd authentication. Nevertheless stick to the specification.                  */
if|if
condition|(
name|isHttpBasicAuthentication
argument_list|()
condition|)
block|{
name|writer
operator|.
name|writeStartElement
argument_list|(
name|prefix
argument_list|,
name|SPConstants
operator|.
name|HTTP_BASIC_AUTHENTICATION
operator|.
name|getLocalPart
argument_list|()
argument_list|,
name|namespaceURI
argument_list|)
expr_stmt|;
name|writer
operator|.
name|writeEndElement
argument_list|()
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|isHttpDigestAuthentication
argument_list|()
condition|)
block|{
name|writer
operator|.
name|writeStartElement
argument_list|(
name|prefix
argument_list|,
name|SPConstants
operator|.
name|HTTP_DIGEST_AUTHENTICATION
operator|.
name|getLocalPart
argument_list|()
argument_list|,
name|namespaceURI
argument_list|)
expr_stmt|;
name|writer
operator|.
name|writeEndElement
argument_list|()
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|isRequireClientCertificate
argument_list|()
condition|)
block|{
name|writer
operator|.
name|writeStartElement
argument_list|(
name|prefix
argument_list|,
name|SPConstants
operator|.
name|REQUIRE_CLIENT_CERTIFICATE
operator|.
name|getLocalPart
argument_list|()
argument_list|,
name|namespaceURI
argument_list|)
expr_stmt|;
name|writer
operator|.
name|writeEndElement
argument_list|()
expr_stmt|;
block|}
comment|//</wsp:Policy>
name|writer
operator|.
name|writeEndElement
argument_list|()
expr_stmt|;
block|}
block|}
else|else
block|{
comment|// RequireClientCertificate=".."
name|writer
operator|.
name|writeAttribute
argument_list|(
name|SPConstants
operator|.
name|REQUIRE_CLIENT_CERTIFICATE
operator|.
name|getLocalPart
argument_list|()
argument_list|,
name|Boolean
operator|.
name|toString
argument_list|(
name|isRequireClientCertificate
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|writer
operator|.
name|writeEndElement
argument_list|()
expr_stmt|;
comment|//</sp:HttpsToken>
block|}
block|}
end_class

end_unit

