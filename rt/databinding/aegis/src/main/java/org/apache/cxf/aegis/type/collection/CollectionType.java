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
name|collection
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|ArrayList
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Collection
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|HashSet
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Iterator
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|LinkedList
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
name|java
operator|.
name|util
operator|.
name|Set
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|SortedSet
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Stack
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|TreeSet
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Vector
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
name|type
operator|.
name|basic
operator|.
name|ArrayType
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

begin_class
specifier|public
class|class
name|CollectionType
extends|extends
name|ArrayType
block|{
specifier|private
name|AegisType
name|componentType
decl_stmt|;
specifier|public
name|CollectionType
parameter_list|(
name|AegisType
name|componentType
parameter_list|)
block|{
name|super
argument_list|()
expr_stmt|;
name|this
operator|.
name|componentType
operator|=
name|componentType
expr_stmt|;
block|}
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
throws|throws
name|DatabindingException
block|{
try|try
block|{
return|return
name|readCollection
argument_list|(
name|reader
argument_list|,
literal|null
argument_list|,
name|context
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|IllegalArgumentException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|DatabindingException
argument_list|(
literal|"Illegal argument."
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
annotation|@
name|Override
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
specifier|protected
name|Collection
argument_list|<
name|Object
argument_list|>
name|createCollection
parameter_list|()
block|{
name|Collection
argument_list|<
name|Object
argument_list|>
name|values
init|=
literal|null
decl_stmt|;
comment|/*          * getTypeClass returns the type of the object. These 'if's asked if the proposed          * type can be assigned to the object, not the other way around. Thus List before          * Vector and Set before SortedSet.          */
name|Class
argument_list|<
name|?
argument_list|>
name|userTypeClass
init|=
name|getTypeClass
argument_list|()
decl_stmt|;
if|if
condition|(
name|userTypeClass
operator|.
name|isAssignableFrom
argument_list|(
name|List
operator|.
name|class
argument_list|)
condition|)
block|{
name|values
operator|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|userTypeClass
operator|.
name|isAssignableFrom
argument_list|(
name|LinkedList
operator|.
name|class
argument_list|)
condition|)
block|{
name|values
operator|=
operator|new
name|LinkedList
argument_list|<
name|Object
argument_list|>
argument_list|()
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|userTypeClass
operator|.
name|isAssignableFrom
argument_list|(
name|Set
operator|.
name|class
argument_list|)
condition|)
block|{
name|values
operator|=
operator|new
name|HashSet
argument_list|<>
argument_list|()
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|userTypeClass
operator|.
name|isAssignableFrom
argument_list|(
name|SortedSet
operator|.
name|class
argument_list|)
condition|)
block|{
name|values
operator|=
operator|new
name|TreeSet
argument_list|<
name|Object
argument_list|>
argument_list|()
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|userTypeClass
operator|.
name|isAssignableFrom
argument_list|(
name|Vector
operator|.
name|class
argument_list|)
condition|)
block|{
name|values
operator|=
operator|new
name|Vector
argument_list|<
name|Object
argument_list|>
argument_list|()
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|userTypeClass
operator|.
name|isAssignableFrom
argument_list|(
name|Stack
operator|.
name|class
argument_list|)
condition|)
block|{
name|values
operator|=
operator|new
name|Stack
argument_list|<
name|Object
argument_list|>
argument_list|()
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|userTypeClass
operator|.
name|isInterface
argument_list|()
condition|)
block|{
name|values
operator|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
expr_stmt|;
block|}
else|else
block|{
try|try
block|{
name|values
operator|=
operator|(
name|Collection
argument_list|<
name|Object
argument_list|>
operator|)
name|userTypeClass
operator|.
name|newInstance
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|DatabindingException
argument_list|(
literal|"Could not create map implementation: "
operator|+
name|userTypeClass
operator|.
name|getName
argument_list|()
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
return|return
name|values
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
throws|throws
name|DatabindingException
block|{
if|if
condition|(
name|object
operator|==
literal|null
condition|)
block|{
return|return;
block|}
try|try
block|{
name|Collection
argument_list|<
name|?
argument_list|>
name|list
init|=
operator|(
name|Collection
argument_list|<
name|?
argument_list|>
operator|)
name|object
decl_stmt|;
name|AegisType
name|type
init|=
name|getComponentType
argument_list|()
decl_stmt|;
if|if
condition|(
name|type
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|DatabindingException
argument_list|(
literal|"Couldn't find component type for Collection."
argument_list|)
throw|;
block|}
for|for
control|(
name|Iterator
argument_list|<
name|?
argument_list|>
name|itr
init|=
name|list
operator|.
name|iterator
argument_list|()
init|;
name|itr
operator|.
name|hasNext
argument_list|()
condition|;
control|)
block|{
name|String
name|ns
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|type
operator|.
name|isAbstract
argument_list|()
condition|)
block|{
name|ns
operator|=
name|getSchemaType
argument_list|()
operator|.
name|getNamespaceURI
argument_list|()
expr_stmt|;
block|}
else|else
block|{
name|ns
operator|=
name|type
operator|.
name|getSchemaType
argument_list|()
operator|.
name|getNamespaceURI
argument_list|()
expr_stmt|;
block|}
name|writeValue
argument_list|(
name|itr
operator|.
name|next
argument_list|()
argument_list|,
name|writer
argument_list|,
name|context
argument_list|,
name|type
argument_list|,
name|type
operator|.
name|getSchemaType
argument_list|()
operator|.
name|getLocalPart
argument_list|()
argument_list|,
name|ns
argument_list|)
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|IllegalArgumentException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|DatabindingException
argument_list|(
literal|"Illegal argument."
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
annotation|@
name|Override
specifier|public
name|AegisType
name|getComponentType
parameter_list|()
block|{
return|return
name|componentType
return|;
block|}
block|}
end_class

end_unit

