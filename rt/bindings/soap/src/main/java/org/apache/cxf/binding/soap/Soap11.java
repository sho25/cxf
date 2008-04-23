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
name|binding
operator|.
name|soap
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

begin_comment
comment|/**  * Singleton object that represents the SOAP 1.1 version.  *   */
end_comment

begin_class
specifier|public
specifier|final
class|class
name|Soap11
implements|implements
name|SoapVersion
block|{
comment|// some constants that don't fit into the SoapVersion paradigm.
specifier|public
specifier|static
specifier|final
name|String
name|SOAP_NAMESPACE
init|=
literal|"http://schemas.xmlsoap.org/soap/envelope/"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|SOAP_ENCODING_URI
init|=
literal|"http://schemas.xmlsoap.org/soap/encoding/"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|QName
name|ENCODED_STRING
init|=
operator|new
name|QName
argument_list|(
name|SOAP_ENCODING_URI
argument_list|,
literal|"string"
argument_list|)
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|QName
name|ENCODED_LONG
init|=
operator|new
name|QName
argument_list|(
name|SOAP_ENCODING_URI
argument_list|,
literal|"long"
argument_list|)
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|QName
name|ENCODED_FLOAT
init|=
operator|new
name|QName
argument_list|(
name|SOAP_ENCODING_URI
argument_list|,
literal|"float"
argument_list|)
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|QName
name|ENCODED_CHAR
init|=
operator|new
name|QName
argument_list|(
name|SOAP_ENCODING_URI
argument_list|,
literal|"char"
argument_list|)
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|QName
name|ENCODED_DOUBLE
init|=
operator|new
name|QName
argument_list|(
name|SOAP_ENCODING_URI
argument_list|,
literal|"double"
argument_list|)
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|QName
name|ENCODED_INT
init|=
operator|new
name|QName
argument_list|(
name|SOAP_ENCODING_URI
argument_list|,
literal|"int"
argument_list|)
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|QName
name|ENCODED_SHORT
init|=
operator|new
name|QName
argument_list|(
name|SOAP_ENCODING_URI
argument_list|,
literal|"short"
argument_list|)
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|QName
name|ENCODED_BOOLEAN
init|=
operator|new
name|QName
argument_list|(
name|SOAP_ENCODING_URI
argument_list|,
literal|"boolean"
argument_list|)
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|QName
name|ENCODED_DATETIME
init|=
operator|new
name|QName
argument_list|(
name|SOAP_ENCODING_URI
argument_list|,
literal|"dateTime"
argument_list|)
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|QName
name|ENCODED_BASE64
init|=
operator|new
name|QName
argument_list|(
name|SOAP_ENCODING_URI
argument_list|,
literal|"base64Binary"
argument_list|)
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|QName
name|ENCODED_DECIMAL
init|=
operator|new
name|QName
argument_list|(
name|SOAP_ENCODING_URI
argument_list|,
literal|"decimal"
argument_list|)
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|QName
name|ENCODED_INTEGER
init|=
operator|new
name|QName
argument_list|(
name|SOAP_ENCODING_URI
argument_list|,
literal|"integer"
argument_list|)
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|Soap11
name|INSTANCE
init|=
operator|new
name|Soap11
argument_list|()
decl_stmt|;
specifier|private
specifier|final
name|double
name|version
init|=
literal|1.1
decl_stmt|;
specifier|private
specifier|final
name|String
name|namespace
init|=
name|SOAP_NAMESPACE
decl_stmt|;
specifier|private
specifier|final
name|String
name|prefix
init|=
literal|"soap"
decl_stmt|;
specifier|private
specifier|final
name|String
name|noneRole
init|=
name|namespace
operator|+
literal|"/role/none"
decl_stmt|;
specifier|private
specifier|final
name|String
name|ultimateReceiverRole
init|=
name|namespace
operator|+
literal|"/role/ultimateReceiver"
decl_stmt|;
specifier|private
specifier|final
name|String
name|nextRole
init|=
literal|"http://schemas.xmlsoap.org/soap/actor/next"
decl_stmt|;
specifier|private
specifier|final
name|String
name|soapEncodingStyle
init|=
name|SOAP_ENCODING_URI
decl_stmt|;
specifier|private
specifier|final
name|QName
name|envelope
init|=
operator|new
name|QName
argument_list|(
name|namespace
argument_list|,
literal|"Envelope"
argument_list|,
name|prefix
argument_list|)
decl_stmt|;
specifier|private
specifier|final
name|QName
name|header
init|=
operator|new
name|QName
argument_list|(
name|namespace
argument_list|,
literal|"Header"
argument_list|,
name|prefix
argument_list|)
decl_stmt|;
specifier|private
specifier|final
name|QName
name|body
init|=
operator|new
name|QName
argument_list|(
name|namespace
argument_list|,
literal|"Body"
argument_list|,
name|prefix
argument_list|)
decl_stmt|;
specifier|private
specifier|final
name|QName
name|fault
init|=
operator|new
name|QName
argument_list|(
name|namespace
argument_list|,
literal|"Fault"
argument_list|,
name|prefix
argument_list|)
decl_stmt|;
specifier|private
name|Soap11
parameter_list|()
block|{
comment|// Singleton
block|}
specifier|public
specifier|static
name|Soap11
name|getInstance
parameter_list|()
block|{
return|return
name|INSTANCE
return|;
block|}
specifier|public
name|double
name|getVersion
parameter_list|()
block|{
return|return
name|version
return|;
block|}
specifier|public
name|String
name|getNamespace
parameter_list|()
block|{
return|return
name|namespace
return|;
block|}
specifier|public
name|String
name|getPrefix
parameter_list|()
block|{
return|return
name|prefix
return|;
block|}
specifier|public
name|QName
name|getEnvelope
parameter_list|()
block|{
return|return
name|envelope
return|;
block|}
specifier|public
name|QName
name|getHeader
parameter_list|()
block|{
return|return
name|header
return|;
block|}
specifier|public
name|QName
name|getBody
parameter_list|()
block|{
return|return
name|body
return|;
block|}
specifier|public
name|QName
name|getFault
parameter_list|()
block|{
return|return
name|fault
return|;
block|}
specifier|public
name|String
name|getSoapEncodingStyle
parameter_list|()
block|{
return|return
name|soapEncodingStyle
return|;
block|}
comment|// Role URIs
comment|// -------------------------------------------------------------------------
specifier|public
name|String
name|getNoneRole
parameter_list|()
block|{
return|return
name|noneRole
return|;
block|}
specifier|public
name|String
name|getUltimateReceiverRole
parameter_list|()
block|{
return|return
name|ultimateReceiverRole
return|;
block|}
specifier|public
name|String
name|getNextRole
parameter_list|()
block|{
return|return
name|nextRole
return|;
block|}
specifier|public
name|String
name|getAttrNameRole
parameter_list|()
block|{
return|return
literal|"actor"
return|;
block|}
specifier|public
name|String
name|getAttrNameMustUnderstand
parameter_list|()
block|{
return|return
literal|"mustUnderstand"
return|;
block|}
specifier|public
name|QName
name|getReceiver
parameter_list|()
block|{
return|return
operator|new
name|QName
argument_list|(
name|SOAP_NAMESPACE
argument_list|,
literal|"Server"
argument_list|)
return|;
block|}
specifier|public
name|QName
name|getSender
parameter_list|()
block|{
return|return
operator|new
name|QName
argument_list|(
name|SOAP_NAMESPACE
argument_list|,
literal|"Client"
argument_list|)
return|;
block|}
specifier|public
name|QName
name|getMustUnderstand
parameter_list|()
block|{
return|return
operator|new
name|QName
argument_list|(
name|SOAP_NAMESPACE
argument_list|,
literal|"MustUnderstand"
argument_list|)
return|;
block|}
specifier|public
name|QName
name|getVersionMismatch
parameter_list|()
block|{
return|return
operator|new
name|QName
argument_list|(
name|SOAP_NAMESPACE
argument_list|,
literal|"VersionMismatch"
argument_list|)
return|;
block|}
specifier|public
name|QName
name|getDateEncodingUnknown
parameter_list|()
block|{
comment|// There is no such fault code in soap11
return|return
literal|null
return|;
block|}
specifier|public
name|String
name|getContentType
parameter_list|()
block|{
return|return
literal|"text/xml"
return|;
block|}
block|}
end_class

end_unit

