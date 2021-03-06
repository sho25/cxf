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
name|xkms
operator|.
name|x509
operator|.
name|repo
operator|.
name|ldap
package|;
end_package

begin_class
specifier|public
class|class
name|LdapSchemaConfig
block|{
specifier|private
name|String
name|certObjectClass
init|=
literal|"inetOrgPerson"
decl_stmt|;
specifier|private
name|String
name|attrUID
init|=
literal|"uid"
decl_stmt|;
specifier|private
name|String
name|attrIssuerID
init|=
literal|"manager"
decl_stmt|;
specifier|private
name|String
name|attrSerialNumber
init|=
literal|"employeeNumber"
decl_stmt|;
specifier|private
name|String
name|attrEndpoint
init|=
literal|"labeledURI"
decl_stmt|;
specifier|private
name|String
name|attrCrtBinary
init|=
literal|"userCertificate;binary"
decl_stmt|;
specifier|private
name|String
name|attrCrlBinary
init|=
literal|"certificateRevocationList;binary"
decl_stmt|;
specifier|private
name|String
name|constAttrNamesCSV
init|=
literal|"sn"
decl_stmt|;
specifier|private
name|String
name|constAttrValuesCSV
init|=
literal|"X509 certificate"
decl_stmt|;
specifier|private
name|String
name|serviceCertRDNTemplate
init|=
literal|"cn=%s,ou=services"
decl_stmt|;
specifier|private
name|String
name|serviceCertUIDTemplate
init|=
literal|"uid=%s"
decl_stmt|;
specifier|private
name|String
name|trustedAuthorityFilter
init|=
literal|"(&(objectClass=inetOrgPerson)(ou:dn:=CAs))"
decl_stmt|;
specifier|private
name|String
name|intermediateFilter
init|=
literal|"(objectClass=*)"
decl_stmt|;
specifier|private
name|String
name|crlFilter
init|=
literal|"(&(objectClass=inetOrgPerson)(ou:dn:=CAs))"
decl_stmt|;
specifier|public
name|String
name|getCertObjectClass
parameter_list|()
block|{
return|return
name|certObjectClass
return|;
block|}
specifier|public
name|void
name|setCertObjectClass
parameter_list|(
name|String
name|crtObjectClass
parameter_list|)
block|{
name|this
operator|.
name|certObjectClass
operator|=
name|crtObjectClass
expr_stmt|;
block|}
specifier|public
name|String
name|getAttrUID
parameter_list|()
block|{
return|return
name|attrUID
return|;
block|}
specifier|public
name|void
name|setAttrUID
parameter_list|(
name|String
name|attrUID
parameter_list|)
block|{
name|this
operator|.
name|attrUID
operator|=
name|attrUID
expr_stmt|;
block|}
specifier|public
name|String
name|getAttrIssuerID
parameter_list|()
block|{
return|return
name|attrIssuerID
return|;
block|}
specifier|public
name|void
name|setAttrIssuerID
parameter_list|(
name|String
name|attrIssuerID
parameter_list|)
block|{
name|this
operator|.
name|attrIssuerID
operator|=
name|attrIssuerID
expr_stmt|;
block|}
specifier|public
name|String
name|getAttrSerialNumber
parameter_list|()
block|{
return|return
name|attrSerialNumber
return|;
block|}
specifier|public
name|void
name|setAttrSerialNumber
parameter_list|(
name|String
name|attrSerialNumber
parameter_list|)
block|{
name|this
operator|.
name|attrSerialNumber
operator|=
name|attrSerialNumber
expr_stmt|;
block|}
specifier|public
name|String
name|getAttrCrtBinary
parameter_list|()
block|{
return|return
name|attrCrtBinary
return|;
block|}
specifier|public
name|void
name|setAttrCrtBinary
parameter_list|(
name|String
name|attrCrtBinary
parameter_list|)
block|{
name|this
operator|.
name|attrCrtBinary
operator|=
name|attrCrtBinary
expr_stmt|;
block|}
specifier|public
name|String
name|getConstAttrNamesCSV
parameter_list|()
block|{
return|return
name|constAttrNamesCSV
return|;
block|}
specifier|public
name|void
name|setConstAttrNamesCSV
parameter_list|(
name|String
name|constAttrNamesCSV
parameter_list|)
block|{
name|this
operator|.
name|constAttrNamesCSV
operator|=
name|constAttrNamesCSV
expr_stmt|;
block|}
specifier|public
name|String
name|getConstAttrValuesCSV
parameter_list|()
block|{
return|return
name|constAttrValuesCSV
return|;
block|}
specifier|public
name|void
name|setConstAttrValuesCSV
parameter_list|(
name|String
name|constAttrValuesCSV
parameter_list|)
block|{
name|this
operator|.
name|constAttrValuesCSV
operator|=
name|constAttrValuesCSV
expr_stmt|;
block|}
specifier|public
name|String
name|getServiceCertRDNTemplate
parameter_list|()
block|{
return|return
name|serviceCertRDNTemplate
return|;
block|}
specifier|public
name|void
name|setServiceCertRDNTemplate
parameter_list|(
name|String
name|serviceCrtRDNTemplate
parameter_list|)
block|{
name|this
operator|.
name|serviceCertRDNTemplate
operator|=
name|serviceCrtRDNTemplate
expr_stmt|;
block|}
specifier|public
name|String
name|getServiceCertUIDTemplate
parameter_list|()
block|{
return|return
name|serviceCertUIDTemplate
return|;
block|}
specifier|public
name|void
name|setServiceCertUIDTemplate
parameter_list|(
name|String
name|serviceCrtUIDTemplate
parameter_list|)
block|{
name|this
operator|.
name|serviceCertUIDTemplate
operator|=
name|serviceCrtUIDTemplate
expr_stmt|;
block|}
specifier|public
name|String
name|getTrustedAuthorityFilter
parameter_list|()
block|{
return|return
name|trustedAuthorityFilter
return|;
block|}
specifier|public
name|void
name|setTrustedAuthorityFilter
parameter_list|(
name|String
name|trustedAuthorityFilter
parameter_list|)
block|{
name|this
operator|.
name|trustedAuthorityFilter
operator|=
name|trustedAuthorityFilter
expr_stmt|;
block|}
specifier|public
name|String
name|getIntermediateFilter
parameter_list|()
block|{
return|return
name|intermediateFilter
return|;
block|}
specifier|public
name|void
name|setIntermediateFilter
parameter_list|(
name|String
name|intermediateFilter
parameter_list|)
block|{
name|this
operator|.
name|intermediateFilter
operator|=
name|intermediateFilter
expr_stmt|;
block|}
specifier|public
name|String
name|getCrlFilter
parameter_list|()
block|{
return|return
name|crlFilter
return|;
block|}
specifier|public
name|void
name|setCrlFilter
parameter_list|(
name|String
name|crlFilter
parameter_list|)
block|{
name|this
operator|.
name|crlFilter
operator|=
name|crlFilter
expr_stmt|;
block|}
specifier|public
name|String
name|getAttrCrlBinary
parameter_list|()
block|{
return|return
name|attrCrlBinary
return|;
block|}
specifier|public
name|void
name|setAttrCrlBinary
parameter_list|(
name|String
name|attrCrlBinary
parameter_list|)
block|{
name|this
operator|.
name|attrCrlBinary
operator|=
name|attrCrlBinary
expr_stmt|;
block|}
specifier|public
name|String
name|getAttrEndpoint
parameter_list|()
block|{
return|return
name|attrEndpoint
return|;
block|}
specifier|public
name|void
name|setAttrEndpoint
parameter_list|(
name|String
name|attrEndpoint
parameter_list|)
block|{
name|this
operator|.
name|attrEndpoint
operator|=
name|attrEndpoint
expr_stmt|;
block|}
block|}
end_class

end_unit

