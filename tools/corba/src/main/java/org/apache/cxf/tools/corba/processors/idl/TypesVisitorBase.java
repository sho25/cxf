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
name|tools
operator|.
name|corba
operator|.
name|processors
operator|.
name|idl
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
name|antlr
operator|.
name|ASTVisitor
import|;
end_import

begin_import
import|import
name|antlr
operator|.
name|collections
operator|.
name|AST
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
name|corba
operator|.
name|wsdl
operator|.
name|CorbaTypeImpl
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
name|corba
operator|.
name|wsdl
operator|.
name|TypeMappingType
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
name|XmlSchemaCollection
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
name|XmlSchemaType
import|;
end_import

begin_class
specifier|public
specifier|abstract
class|class
name|TypesVisitorBase
implements|implements
name|ASTVisitor
block|{
specifier|protected
name|XmlSchema
name|schema
decl_stmt|;
specifier|protected
name|XmlSchemaCollection
name|schemas
decl_stmt|;
specifier|protected
name|TypeMappingType
name|typeMap
decl_stmt|;
name|XmlSchemaType
name|schemaType
decl_stmt|;
name|CorbaTypeImpl
name|corbaType
decl_stmt|;
specifier|public
name|TypesVisitorBase
parameter_list|(
name|XmlSchemaCollection
name|xmlSchemas
parameter_list|,
name|XmlSchema
name|xmlSchema
parameter_list|,
name|TypeMappingType
name|typeMapRef
parameter_list|)
block|{
name|schemas
operator|=
name|xmlSchemas
expr_stmt|;
name|schema
operator|=
name|xmlSchema
expr_stmt|;
name|typeMap
operator|=
name|typeMapRef
expr_stmt|;
block|}
specifier|public
specifier|abstract
name|void
name|visit
parameter_list|(
name|AST
name|node
parameter_list|)
function_decl|;
specifier|public
name|XmlSchema
name|getSchema
parameter_list|()
block|{
return|return
name|schema
return|;
block|}
specifier|public
name|TypeMappingType
name|getCorbaTypeMap
parameter_list|()
block|{
return|return
name|typeMap
return|;
block|}
specifier|public
name|XmlSchemaType
name|getSchemaType
parameter_list|()
block|{
return|return
name|schemaType
return|;
block|}
specifier|public
name|CorbaTypeImpl
name|getCorbaType
parameter_list|()
block|{
return|return
name|corbaType
return|;
block|}
specifier|public
name|QName
name|getSchemaTypeName
parameter_list|()
block|{
return|return
name|schemaType
operator|.
name|getQName
argument_list|()
return|;
block|}
specifier|public
name|QName
name|getCorbaTypeName
parameter_list|()
block|{
return|return
name|corbaType
operator|.
name|getQName
argument_list|()
return|;
block|}
specifier|public
name|void
name|setSchemaType
parameter_list|(
name|XmlSchemaType
name|type
parameter_list|)
block|{
name|schemaType
operator|=
name|type
expr_stmt|;
block|}
specifier|public
name|void
name|setCorbaType
parameter_list|(
name|CorbaTypeImpl
name|type
parameter_list|)
block|{
name|corbaType
operator|=
name|type
expr_stmt|;
block|}
block|}
end_class

end_unit

