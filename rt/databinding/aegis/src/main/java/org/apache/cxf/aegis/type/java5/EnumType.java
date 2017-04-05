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
name|java
operator|.
name|lang
operator|.
name|reflect
operator|.
name|Type
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
name|AegisType
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
name|XmlSchemaFacet
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
name|constants
operator|.
name|Constants
import|;
end_import

begin_class
specifier|public
class|class
name|EnumType
extends|extends
name|AegisType
block|{
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
name|matchValue
argument_list|(
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
name|getValue
argument_list|(
operator|(
name|Enum
argument_list|<
name|?
argument_list|>
operator|)
name|object
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|setTypeClass
parameter_list|(
name|Type
name|typeClass
parameter_list|)
block|{
if|if
condition|(
operator|!
operator|(
name|typeClass
operator|instanceof
name|Class
operator|)
condition|)
block|{
throw|throw
operator|new
name|DatabindingException
argument_list|(
literal|"Aegis cannot map generic Enums."
argument_list|)
throw|;
block|}
name|Class
argument_list|<
name|?
argument_list|>
name|plainClass
init|=
operator|(
name|Class
argument_list|<
name|?
argument_list|>
operator|)
name|typeClass
decl_stmt|;
if|if
condition|(
operator|!
name|plainClass
operator|.
name|isEnum
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|DatabindingException
argument_list|(
literal|"EnumType must map an enum."
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
argument_list|,
literal|true
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
name|Constants
operator|.
name|XSD_STRING
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
name|List
argument_list|<
name|XmlSchemaFacet
argument_list|>
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
name|getValue
argument_list|(
name|constant
argument_list|)
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
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
specifier|private
name|Enum
argument_list|<
name|?
argument_list|>
name|matchValue
parameter_list|(
name|String
name|value
parameter_list|)
block|{
if|if
condition|(
name|value
operator|==
literal|null
condition|)
block|{
return|return
literal|null
return|;
block|}
annotation|@
name|SuppressWarnings
argument_list|(
literal|"rawtypes"
argument_list|)
name|Class
argument_list|<
name|?
extends|extends
name|Enum
argument_list|>
name|enumClass
init|=
operator|(
name|Class
argument_list|<
name|?
extends|extends
name|Enum
argument_list|>
operator|)
name|getTypeClass
argument_list|()
decl_stmt|;
for|for
control|(
name|Enum
argument_list|<
name|?
argument_list|>
name|enumConstant
range|:
name|enumClass
operator|.
name|getEnumConstants
argument_list|()
control|)
block|{
if|if
condition|(
name|value
operator|.
name|equals
argument_list|(
name|AnnotationReader
operator|.
name|getEnumValue
argument_list|(
name|enumConstant
argument_list|)
argument_list|)
condition|)
block|{
return|return
name|enumConstant
return|;
block|}
block|}
return|return
name|Enum
operator|.
name|valueOf
argument_list|(
name|enumClass
argument_list|,
name|value
operator|.
name|trim
argument_list|()
argument_list|)
return|;
block|}
specifier|private
name|Object
name|getValue
parameter_list|(
name|Object
name|constant
parameter_list|)
block|{
if|if
condition|(
operator|!
operator|(
name|constant
operator|instanceof
name|Enum
argument_list|<
name|?
argument_list|>
operator|)
condition|)
block|{
return|return
literal|null
return|;
block|}
name|Enum
argument_list|<
name|?
argument_list|>
name|enumConstant
init|=
operator|(
name|Enum
argument_list|<
name|?
argument_list|>
operator|)
name|constant
decl_stmt|;
name|String
name|annotatedValue
init|=
name|AnnotationReader
operator|.
name|getEnumValue
argument_list|(
name|enumConstant
argument_list|)
decl_stmt|;
if|if
condition|(
name|annotatedValue
operator|!=
literal|null
condition|)
block|{
return|return
name|annotatedValue
return|;
block|}
return|return
name|enumConstant
operator|.
name|name
argument_list|()
return|;
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

