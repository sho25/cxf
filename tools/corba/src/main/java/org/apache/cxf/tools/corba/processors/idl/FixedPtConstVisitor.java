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
name|wsdl
operator|.
name|Definition
import|;
end_import

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
name|CorbaConstants
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
name|CorbaType
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
name|FixedPtConstVisitor
implements|implements
name|Visitor
block|{
specifier|private
name|XmlSchemaType
name|schemaType
decl_stmt|;
specifier|private
name|CorbaType
name|corbaType
decl_stmt|;
specifier|private
name|Scope
name|scope
decl_stmt|;
specifier|private
name|XmlSchemaCollection
name|schemas
decl_stmt|;
specifier|public
name|FixedPtConstVisitor
parameter_list|(
name|Scope
name|scopeRef
parameter_list|,
name|Definition
name|defn
parameter_list|,
name|XmlSchema
name|schemaRef
parameter_list|,
name|XmlSchemaCollection
name|xmlSchemas
parameter_list|)
block|{
name|scope
operator|=
name|scopeRef
expr_stmt|;
name|schemas
operator|=
name|xmlSchemas
expr_stmt|;
block|}
specifier|public
specifier|static
name|boolean
name|accept
parameter_list|(
name|AST
name|node
parameter_list|)
block|{
if|if
condition|(
name|node
operator|.
name|getType
argument_list|()
operator|==
name|IDLTokenTypes
operator|.
name|LITERAL_fixed
condition|)
block|{
return|return
literal|true
return|;
block|}
return|return
literal|false
return|;
block|}
specifier|public
name|void
name|visit
parameter_list|(
name|AST
name|fixedNode
parameter_list|)
block|{
comment|//<fixed_pt_const_type> ::= "fixed"
name|XmlSchemaType
name|stype
init|=
literal|null
decl_stmt|;
name|CorbaType
name|ctype
init|=
literal|null
decl_stmt|;
name|QName
name|corbaTypeQName
init|=
name|CorbaConstants
operator|.
name|NE_CORBA_FIXED
decl_stmt|;
if|if
condition|(
name|corbaTypeQName
operator|!=
literal|null
condition|)
block|{
name|QName
name|schemaTypeQName
init|=
name|Constants
operator|.
name|XSD_DECIMAL
decl_stmt|;
if|if
condition|(
name|schemaTypeQName
operator|!=
literal|null
condition|)
block|{
name|stype
operator|=
name|schemas
operator|.
name|getTypeByQName
argument_list|(
name|schemaTypeQName
argument_list|)
expr_stmt|;
if|if
condition|(
name|stype
operator|!=
literal|null
condition|)
block|{
name|ctype
operator|=
operator|new
name|CorbaType
argument_list|()
expr_stmt|;
name|ctype
operator|.
name|setQName
argument_list|(
name|corbaTypeQName
argument_list|)
expr_stmt|;
name|ctype
operator|.
name|setType
argument_list|(
name|stype
operator|.
name|getQName
argument_list|()
argument_list|)
expr_stmt|;
name|ctype
operator|.
name|setName
argument_list|(
name|stype
operator|.
name|getQName
argument_list|()
operator|.
name|getLocalPart
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
name|schemaType
operator|=
name|stype
expr_stmt|;
name|corbaType
operator|=
name|ctype
expr_stmt|;
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
name|CorbaType
name|getCorbaType
parameter_list|()
block|{
return|return
name|corbaType
return|;
block|}
specifier|public
name|Scope
name|getScope
parameter_list|()
block|{
return|return
name|scope
return|;
block|}
specifier|public
name|Scope
name|getFullyQualifiedName
parameter_list|()
block|{
return|return
name|scope
return|;
block|}
block|}
end_class

end_unit

