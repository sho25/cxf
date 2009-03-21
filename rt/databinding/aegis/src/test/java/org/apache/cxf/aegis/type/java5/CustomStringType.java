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
name|java5
package|;
end_package

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|XMLConstants
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
name|type
operator|.
name|basic
operator|.
name|StringType
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
name|common
operator|.
name|xmlschema
operator|.
name|XmlSchemaConstants
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|ws
operator|.
name|commons
operator|.
name|schema
operator|.
name|XmlSchema
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|ws
operator|.
name|commons
operator|.
name|schema
operator|.
name|XmlSchemaSimpleContentExtension
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|ws
operator|.
name|commons
operator|.
name|schema
operator|.
name|XmlSchemaSimpleType
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|ws
operator|.
name|commons
operator|.
name|schema
operator|.
name|XmlSchemaSimpleTypeRestriction
import|;
end_import

begin_class
specifier|public
class|class
name|CustomStringType
extends|extends
name|StringType
block|{
annotation|@
name|Override
specifier|public
name|void
name|writeSchema
parameter_list|(
name|XmlSchema
name|root
parameter_list|)
block|{
comment|// this mapping gets used with xs:string, and we might get called.
if|if
condition|(
name|root
operator|.
name|getTargetNamespace
argument_list|()
operator|.
name|equals
argument_list|(
name|XMLConstants
operator|.
name|W3C_XML_SCHEMA_NS_URI
argument_list|)
condition|)
block|{
return|return;
block|}
name|XmlSchemaSimpleType
name|type
init|=
operator|new
name|XmlSchemaSimpleType
argument_list|(
name|root
argument_list|)
decl_stmt|;
name|type
operator|.
name|setName
argument_list|(
name|getSchemaType
argument_list|()
operator|.
name|getLocalPart
argument_list|()
argument_list|)
expr_stmt|;
name|root
operator|.
name|getItems
argument_list|()
operator|.
name|add
argument_list|(
name|type
argument_list|)
expr_stmt|;
name|root
operator|.
name|addType
argument_list|(
name|type
argument_list|)
expr_stmt|;
name|XmlSchemaSimpleContentExtension
name|ext
init|=
operator|new
name|XmlSchemaSimpleContentExtension
argument_list|()
decl_stmt|;
name|ext
operator|.
name|setBaseTypeName
argument_list|(
name|XmlSchemaConstants
operator|.
name|STRING_QNAME
argument_list|)
expr_stmt|;
name|XmlSchemaSimpleTypeRestriction
name|content
init|=
operator|new
name|XmlSchemaSimpleTypeRestriction
argument_list|()
decl_stmt|;
name|content
operator|.
name|setBaseTypeName
argument_list|(
name|XmlSchemaConstants
operator|.
name|STRING_QNAME
argument_list|)
expr_stmt|;
name|type
operator|.
name|setContent
argument_list|(
name|content
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

