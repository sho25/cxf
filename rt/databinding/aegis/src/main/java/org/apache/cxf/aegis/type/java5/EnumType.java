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
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|aegis
operator|.
name|Context
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
name|DatabindingException
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
name|Type
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
name|XmlSchemaEnumerationFacet
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
name|XmlSchemaObjectCollection
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
name|EnumType
extends|extends
name|Type
block|{
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
annotation|@
name|Override
specifier|public
name|Object
name|readObject
parameter_list|(
name|MessageReader
name|reader
parameter_list|,
name|Context
name|context
parameter_list|)
block|{
name|String
name|value
init|=
name|reader
operator|.
name|getValue
argument_list|()
decl_stmt|;
return|return
name|Enum
operator|.
name|valueOf
argument_list|(
name|getTypeClass
argument_list|()
argument_list|,
name|value
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|writeObject
parameter_list|(
name|Object
name|object
parameter_list|,
name|MessageWriter
name|writer
parameter_list|,
name|Context
name|context
parameter_list|)
block|{
comment|// match the reader.
name|writer
operator|.
name|writeValue
argument_list|(
operator|(
operator|(
name|Enum
operator|)
name|object
operator|)
operator|.
name|name
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|setTypeClass
parameter_list|(
name|Class
name|typeClass
parameter_list|)
block|{
if|if
condition|(
operator|!
name|typeClass
operator|.
name|isEnum
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|DatabindingException
argument_list|(
literal|"Type class must be an enum."
argument_list|)
throw|;
block|}
name|super
operator|.
name|setTypeClass
argument_list|(
name|typeClass
argument_list|)
expr_stmt|;
block|}
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
name|XmlSchemaSimpleType
name|simple
init|=
operator|new
name|XmlSchemaSimpleType
argument_list|(
name|root
argument_list|)
decl_stmt|;
name|simple
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
name|addType
argument_list|(
name|simple
argument_list|)
expr_stmt|;
name|root
operator|.
name|getItems
argument_list|()
operator|.
name|add
argument_list|(
name|simple
argument_list|)
expr_stmt|;
name|XmlSchemaSimpleTypeRestriction
name|restriction
init|=
operator|new
name|XmlSchemaSimpleTypeRestriction
argument_list|()
decl_stmt|;
name|restriction
operator|.
name|setBaseTypeName
argument_list|(
name|XmlSchemaConstants
operator|.
name|STRING_QNAME
argument_list|)
expr_stmt|;
name|simple
operator|.
name|setContent
argument_list|(
name|restriction
argument_list|)
expr_stmt|;
name|Object
index|[]
name|constants
init|=
name|getTypeClass
argument_list|()
operator|.
name|getEnumConstants
argument_list|()
decl_stmt|;
name|XmlSchemaObjectCollection
name|facets
init|=
name|restriction
operator|.
name|getFacets
argument_list|()
decl_stmt|;
for|for
control|(
name|Object
name|constant
range|:
name|constants
control|)
block|{
name|XmlSchemaEnumerationFacet
name|f
init|=
operator|new
name|XmlSchemaEnumerationFacet
argument_list|()
decl_stmt|;
name|f
operator|.
name|setValue
argument_list|(
operator|(
operator|(
name|Enum
operator|)
name|constant
operator|)
operator|.
name|name
argument_list|()
argument_list|)
expr_stmt|;
name|facets
operator|.
name|add
argument_list|(
name|f
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Override
specifier|public
name|boolean
name|isComplex
parameter_list|()
block|{
return|return
literal|true
return|;
block|}
block|}
end_class

end_unit

