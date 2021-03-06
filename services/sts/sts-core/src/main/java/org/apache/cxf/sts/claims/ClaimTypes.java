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
name|sts
operator|.
name|claims
package|;
end_package

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|URI
import|;
end_import

begin_class
specifier|public
specifier|final
class|class
name|ClaimTypes
block|{
comment|/**      * The base XML namespace URI that is used by the claim types      * http://docs.oasis-open.org/imi/identity/v1.0/os/identity-1.0-spec-os.pdf      */
specifier|public
specifier|static
specifier|final
name|URI
name|URI_BASE
init|=
name|URI
operator|.
name|create
argument_list|(
literal|"http://schemas.xmlsoap.org/ws/2005/05/identity/claims"
argument_list|)
decl_stmt|;
comment|/**      * (givenName in [RFC 2256]) Preferred name or first name of a Subject.      * According to RFC 2256: This attribute is used to hold the part of a person's name      * which is not their surname nor middle name.      */
specifier|public
specifier|static
specifier|final
name|URI
name|FIRSTNAME
init|=
name|URI
operator|.
name|create
argument_list|(
literal|"http://schemas.xmlsoap.org/ws/2005/05/identity/claims/givenname"
argument_list|)
decl_stmt|;
comment|/**      * (sn in [RFC 2256]) Surname or family name of a Subject.      * According to RFC 2256: This is the X.500 surname attribute which contains the family name of a person.      */
specifier|public
specifier|static
specifier|final
name|URI
name|LASTNAME
init|=
name|URI
operator|.
name|create
argument_list|(
literal|"http://schemas.xmlsoap.org/ws/2005/05/identity/claims/surname"
argument_list|)
decl_stmt|;
comment|/**      * (mail in inetOrgPerson) Preferred address for the "To:" field of email      * to be sent to the Subject, usually of the form<user>@<domain>.      * According to inetOrgPerson using [RFC 1274]: This attribute type specifies      * an electronic mailbox attribute following the syntax specified in RFC 822.      */
specifier|public
specifier|static
specifier|final
name|URI
name|EMAILADDRESS
init|=
name|URI
operator|.
name|create
argument_list|(
literal|"http://schemas.xmlsoap.org/ws/2005/05/identity/claims/emailaddress"
argument_list|)
decl_stmt|;
comment|/**      * (street in [RFC 2256]) Street address component of a Subject‟s address information.      * According to RFC 2256: This attribute contains the physical address of the object      * to which the entry corresponds, such as an address for package delivery.      */
specifier|public
specifier|static
specifier|final
name|URI
name|STREETADDRESS
init|=
name|URI
operator|.
name|create
argument_list|(
literal|"http://schemas.xmlsoap.org/ws/2005/05/identity/claims/streetaddress"
argument_list|)
decl_stmt|;
comment|/**      * (/ in [RFC 2256]) Locality component of a Subject's address information.      * According to RFC 2256: This attribute contains the name of a locality, such as a city, county or other      * geographic region.      */
specifier|public
specifier|static
specifier|final
name|URI
name|LOCALITY
init|=
name|URI
operator|.
name|create
argument_list|(
literal|"http://schemas.xmlsoap.org/ws/2005/05/identity/claims/locality"
argument_list|)
decl_stmt|;
comment|/**      * (st in [RFC 2256]) Abbreviation for state or province name of a Subject's address information.      * According to RFC 2256: “This attribute contains the full name of a state or province.      * The values SHOULD be coordinated on a national level and if well-known shortcuts exist.      */
specifier|public
specifier|static
specifier|final
name|URI
name|STATE_PROVINCE
init|=
name|URI
operator|.
name|create
argument_list|(
literal|"http://schemas.xmlsoap.org/ws/2005/05/identity/claims/stateorprovince"
argument_list|)
decl_stmt|;
comment|/**      * (postalCode in X.500) Postal code or zip code component of a Subject's address information.      * According to X.500(2001): The postal code attribute type specifies the postal code of the named      * object.      */
specifier|public
specifier|static
specifier|final
name|URI
name|POSTALCODE
init|=
name|URI
operator|.
name|create
argument_list|(
literal|"http://schemas.xmlsoap.org/ws/2005/05/identity/claims/postalcode"
argument_list|)
decl_stmt|;
comment|/**      * (c in [RFC 2256]) Country of a Subject.      * According to RFC 2256: This attribute contains a two-letter ISO 3166 country code.      */
specifier|public
specifier|static
specifier|final
name|URI
name|COUNTRY
init|=
name|URI
operator|.
name|create
argument_list|(
literal|"http://schemas.xmlsoap.org/ws/2005/05/identity/claims/country"
argument_list|)
decl_stmt|;
comment|/**      * (homePhone in inetOrgPerson) Primary or home telephone number of a Subject.      * According to inetOrgPerson using [RFC 1274]: This attribute type specifies a home telephone number      * associated with a person.      */
specifier|public
specifier|static
specifier|final
name|URI
name|HOMEPHONE
init|=
name|URI
operator|.
name|create
argument_list|(
literal|"http://schemas.xmlsoap.org/ws/2005/05/identity/claims/homephone"
argument_list|)
decl_stmt|;
comment|/**      * (telephoneNumber in X.500 Person) Secondary or work telephone number of a Subject.      * According to X.500(2001): This attribute type specifies an office/campus telephone number associated      * with a person.      */
specifier|public
specifier|static
specifier|final
name|URI
name|OTHERPHONE
init|=
name|URI
operator|.
name|create
argument_list|(
literal|"http://schemas.xmlsoap.org/ws/2005/05/identity/claims/otherphone"
argument_list|)
decl_stmt|;
comment|/**      * (mobile in inetOrgPerson) Mobile telephone number of a Subject.      * According to inetOrgPerson using [RFC 1274]: This attribute type specifies a mobile telephone number      * associated with a person.      */
specifier|public
specifier|static
specifier|final
name|URI
name|MOBILEPHONE
init|=
name|URI
operator|.
name|create
argument_list|(
literal|"http://schemas.xmlsoap.org/ws/2005/05/identity/claims/mobilephone"
argument_list|)
decl_stmt|;
comment|/**      * The date of birth of a Subject in a form allowed by the xs:date data type.      */
specifier|public
specifier|static
specifier|final
name|URI
name|DATEOFBIRTH
init|=
name|URI
operator|.
name|create
argument_list|(
literal|"http://schemas.xmlsoap.org/ws/2005/05/identity/claims/dateofbirth"
argument_list|)
decl_stmt|;
comment|/**      * Gender of a Subject that can have any of these exact URI values      *   '0' (meaning unspecified), '1' (meaning Male) or '2' (meaning Female)      */
specifier|public
specifier|static
specifier|final
name|URI
name|GENDER
init|=
name|URI
operator|.
name|create
argument_list|(
literal|"http://schemas.xmlsoap.org/ws/2005/05/identity/claims/gender"
argument_list|)
decl_stmt|;
comment|/**      * A private personal identifier (PPID) that identifies the Subject to a Relying Party.      */
specifier|public
specifier|static
specifier|final
name|URI
name|PRIVATE_PERSONAL_IDENTIFIER
init|=
name|URI
operator|.
name|create
argument_list|(
literal|"http://schemas.xmlsoap.org/ws/2005/05/identity/claims/privatepersonalidentifier"
argument_list|)
decl_stmt|;
comment|/**      * The Web page of a Subject expressed as a URL.      */
specifier|public
specifier|static
specifier|final
name|URI
name|WEB_PAGE
init|=
name|URI
operator|.
name|create
argument_list|(
literal|"http://schemas.xmlsoap.org/ws/2005/05/identity/claims/webpage"
argument_list|)
decl_stmt|;
specifier|private
name|ClaimTypes
parameter_list|()
block|{
comment|// complete
block|}
block|}
end_class

end_unit

