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
name|Anonsequence
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
name|Sequence
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
name|tools
operator|.
name|corba
operator|.
name|common
operator|.
name|ReferenceConstants
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
name|XmlSchemaElement
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
class|class
name|SequenceDeferredAction
implements|implements
name|SchemaDeferredAction
block|{
specifier|protected
name|Anonsequence
name|anonSequence
decl_stmt|;
specifier|protected
name|Sequence
name|sequence
decl_stmt|;
specifier|protected
name|XmlSchemaElement
name|element
decl_stmt|;
specifier|protected
name|XmlSchema
name|schema
decl_stmt|;
specifier|protected
name|XmlSchemaCollection
name|schemas
decl_stmt|;
specifier|public
name|SequenceDeferredAction
parameter_list|(
name|Sequence
name|sequenceType
parameter_list|,
name|Anonsequence
name|anonSequenceType
parameter_list|,
name|XmlSchemaElement
name|elem
parameter_list|)
block|{
name|anonSequence
operator|=
name|anonSequenceType
expr_stmt|;
name|sequence
operator|=
name|sequenceType
expr_stmt|;
name|element
operator|=
name|elem
expr_stmt|;
block|}
specifier|public
name|SequenceDeferredAction
parameter_list|(
name|Anonsequence
name|anonSequenceType
parameter_list|)
block|{
name|anonSequence
operator|=
name|anonSequenceType
expr_stmt|;
block|}
specifier|public
name|SequenceDeferredAction
parameter_list|(
name|Sequence
name|sequenceType
parameter_list|)
block|{
name|sequence
operator|=
name|sequenceType
expr_stmt|;
block|}
specifier|public
name|SequenceDeferredAction
parameter_list|(
name|XmlSchemaElement
name|elem
parameter_list|)
block|{
name|element
operator|=
name|elem
expr_stmt|;
block|}
specifier|public
name|SequenceDeferredAction
parameter_list|(
name|XmlSchemaCollection
name|xmlSchemas
parameter_list|,
name|XmlSchema
name|xmlSchema
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
block|}
specifier|public
name|void
name|execute
parameter_list|(
name|XmlSchemaType
name|stype
parameter_list|,
name|CorbaTypeImpl
name|ctype
parameter_list|)
block|{
if|if
condition|(
name|anonSequence
operator|!=
literal|null
condition|)
block|{
name|anonSequence
operator|.
name|setElemtype
argument_list|(
name|ctype
operator|.
name|getQName
argument_list|()
argument_list|)
expr_stmt|;
comment|// This is needed for recursive types
name|anonSequence
operator|.
name|setType
argument_list|(
name|stype
operator|.
name|getQName
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|sequence
operator|!=
literal|null
condition|)
block|{
name|sequence
operator|.
name|setElemtype
argument_list|(
name|ctype
operator|.
name|getQName
argument_list|()
argument_list|)
expr_stmt|;
comment|// This is needed for recursive types
name|sequence
operator|.
name|setType
argument_list|(
name|stype
operator|.
name|getQName
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|element
operator|!=
literal|null
condition|)
block|{
name|element
operator|.
name|setSchemaTypeName
argument_list|(
name|stype
operator|.
name|getQName
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
name|stype
operator|.
name|getQName
argument_list|()
operator|.
name|equals
argument_list|(
name|ReferenceConstants
operator|.
name|WSADDRESSING_TYPE
argument_list|)
condition|)
block|{
name|element
operator|.
name|setNillable
argument_list|(
literal|true
argument_list|)
expr_stmt|;
block|}
block|}
if|if
condition|(
name|schemas
operator|!=
literal|null
operator|&&
name|schemas
operator|.
name|getTypeByQName
argument_list|(
name|stype
operator|.
name|getQName
argument_list|()
argument_list|)
operator|==
literal|null
condition|)
block|{
name|schema
operator|.
name|getItems
argument_list|()
operator|.
name|add
argument_list|(
name|stype
argument_list|)
expr_stmt|;
name|schema
operator|.
name|addType
argument_list|(
name|stype
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

