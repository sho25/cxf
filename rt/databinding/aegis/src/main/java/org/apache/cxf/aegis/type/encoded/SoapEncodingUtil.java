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
name|aegis
operator|.
name|type
operator|.
name|encoded
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
name|apache
operator|.
name|cxf
operator|.
name|aegis
operator|.
name|xml
operator|.
name|MessageReader
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
name|aegis
operator|.
name|xml
operator|.
name|MessageWriter
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
name|binding
operator|.
name|soap
operator|.
name|Soap12
import|;
end_import

begin_comment
comment|/**  * Utilitiy methods for SOAP reading and writing encoded mesages.  */
end_comment

begin_class
specifier|public
specifier|final
class|class
name|SoapEncodingUtil
block|{
specifier|private
specifier|static
specifier|final
name|String
name|SOAP_ENCODING_NS_1_2
init|=
name|Soap12
operator|.
name|getInstance
argument_list|()
operator|.
name|getSoapEncodingStyle
argument_list|()
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|QName
name|SOAP_ENCODING_ID_1_2
init|=
operator|new
name|QName
argument_list|(
name|SOAP_ENCODING_NS_1_2
argument_list|,
literal|"id"
argument_list|)
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|QName
name|SOAP_ENCODING_ID_1_1
init|=
operator|new
name|QName
argument_list|(
literal|"id"
argument_list|)
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|QName
name|SOAP_ENCODING_REF_1_2
init|=
operator|new
name|QName
argument_list|(
name|SOAP_ENCODING_NS_1_2
argument_list|,
literal|"ref"
argument_list|)
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|QName
name|SOAP_ENCODING_REF_1_1
init|=
operator|new
name|QName
argument_list|(
literal|"href"
argument_list|)
decl_stmt|;
specifier|private
name|SoapEncodingUtil
parameter_list|()
block|{     }
comment|/**      * Reads the SOAP 1.2 or SOAP 1.1 id attribute.      *      * @param reader the stream to read; must not be null      * @return the id or null of neither attribute was present      */
specifier|public
specifier|static
name|String
name|readId
parameter_list|(
name|MessageReader
name|reader
parameter_list|)
block|{
name|String
name|id
init|=
name|readAttributeValue
argument_list|(
name|reader
argument_list|,
name|SOAP_ENCODING_ID_1_2
argument_list|)
decl_stmt|;
if|if
condition|(
name|id
operator|==
literal|null
condition|)
block|{
name|id
operator|=
name|readAttributeValue
argument_list|(
name|reader
argument_list|,
name|SOAP_ENCODING_ID_1_1
argument_list|)
expr_stmt|;
block|}
return|return
name|id
return|;
block|}
comment|/**      * Writes a SOAP 1.1 id attribute.      *      * @param writer the stream to which the id should be written; must not be null      * @param id the id to write; must not be null      */
specifier|public
specifier|static
name|void
name|writeId
parameter_list|(
name|MessageWriter
name|writer
parameter_list|,
name|String
name|id
parameter_list|)
block|{
if|if
condition|(
name|id
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|NullPointerException
argument_list|(
literal|"id is null"
argument_list|)
throw|;
block|}
name|writeAttribute
argument_list|(
name|writer
argument_list|,
name|SOAP_ENCODING_ID_1_1
argument_list|,
name|id
argument_list|)
expr_stmt|;
block|}
comment|/**      * Reads the SOAP 1.2 or SOAP 1.1 reference attribute.      *      * @param reader the stream to read; must not be null      * @return the reference id or null of neither attribute was present      */
specifier|public
specifier|static
name|String
name|readRef
parameter_list|(
name|MessageReader
name|reader
parameter_list|)
block|{
name|String
name|ref
init|=
name|readAttributeValue
argument_list|(
name|reader
argument_list|,
name|SOAP_ENCODING_REF_1_2
argument_list|)
decl_stmt|;
if|if
condition|(
name|ref
operator|==
literal|null
condition|)
block|{
name|ref
operator|=
name|readAttributeValue
argument_list|(
name|reader
argument_list|,
name|SOAP_ENCODING_REF_1_1
argument_list|)
expr_stmt|;
block|}
return|return
name|ref
return|;
block|}
comment|/**      * Writes a SOAP 1.1 ref attribute.      *      * @param writer the stream to which the id should be written; must not be null      * @param refId the reference id to write; must not be null      */
specifier|public
specifier|static
name|void
name|writeRef
parameter_list|(
name|MessageWriter
name|writer
parameter_list|,
name|String
name|refId
parameter_list|)
block|{
if|if
condition|(
name|refId
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|NullPointerException
argument_list|(
literal|"refId is null"
argument_list|)
throw|;
block|}
name|writeAttribute
argument_list|(
name|writer
argument_list|,
name|SOAP_ENCODING_REF_1_1
argument_list|,
name|refId
argument_list|)
expr_stmt|;
block|}
specifier|public
specifier|static
name|String
name|readAttributeValue
parameter_list|(
name|MessageReader
name|reader
parameter_list|,
name|QName
name|name
parameter_list|)
block|{
if|if
condition|(
name|reader
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|NullPointerException
argument_list|(
literal|"reader is null"
argument_list|)
throw|;
block|}
if|if
condition|(
name|name
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|NullPointerException
argument_list|(
literal|"name is null"
argument_list|)
throw|;
block|}
name|MessageReader
name|attributeReader
init|=
name|reader
operator|.
name|getAttributeReader
argument_list|(
name|name
argument_list|)
decl_stmt|;
if|if
condition|(
name|attributeReader
operator|!=
literal|null
condition|)
block|{
name|String
name|value
init|=
name|attributeReader
operator|.
name|getValue
argument_list|()
decl_stmt|;
if|if
condition|(
name|value
operator|!=
literal|null
condition|)
block|{
return|return
name|value
return|;
block|}
block|}
return|return
literal|null
return|;
block|}
specifier|public
specifier|static
name|void
name|writeAttribute
parameter_list|(
name|MessageWriter
name|writer
parameter_list|,
name|QName
name|name
parameter_list|,
name|String
name|value
parameter_list|)
block|{
if|if
condition|(
name|writer
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|NullPointerException
argument_list|(
literal|"writer is null"
argument_list|)
throw|;
block|}
if|if
condition|(
name|name
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|NullPointerException
argument_list|(
literal|"name is null"
argument_list|)
throw|;
block|}
if|if
condition|(
name|value
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|NullPointerException
argument_list|(
literal|"value is null"
argument_list|)
throw|;
block|}
name|MessageWriter
name|attributeWriter
init|=
name|writer
operator|.
name|getAttributeWriter
argument_list|(
name|name
argument_list|)
decl_stmt|;
name|attributeWriter
operator|.
name|writeValue
argument_list|(
name|value
argument_list|)
expr_stmt|;
name|attributeWriter
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
block|}
end_class

end_unit

