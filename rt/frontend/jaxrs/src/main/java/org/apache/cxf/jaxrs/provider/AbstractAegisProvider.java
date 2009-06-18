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
name|jaxrs
operator|.
name|provider
package|;
end_package

begin_import
import|import
name|java
operator|.
name|lang
operator|.
name|annotation
operator|.
name|Annotation
import|;
end_import

begin_import
import|import
name|java
operator|.
name|lang
operator|.
name|reflect
operator|.
name|Array
import|;
end_import

begin_import
import|import
name|java
operator|.
name|lang
operator|.
name|reflect
operator|.
name|GenericArrayType
import|;
end_import

begin_import
import|import
name|java
operator|.
name|lang
operator|.
name|reflect
operator|.
name|ParameterizedType
import|;
end_import

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
name|HashSet
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Map
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
name|WeakHashMap
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|ws
operator|.
name|rs
operator|.
name|core
operator|.
name|Context
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|ws
operator|.
name|rs
operator|.
name|core
operator|.
name|MediaType
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|ws
operator|.
name|rs
operator|.
name|ext
operator|.
name|ContextResolver
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|ws
operator|.
name|rs
operator|.
name|ext
operator|.
name|MessageBodyReader
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|ws
operator|.
name|rs
operator|.
name|ext
operator|.
name|MessageBodyWriter
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
name|AegisContext
import|;
end_import

begin_class
specifier|public
specifier|abstract
class|class
name|AbstractAegisProvider
implements|implements
name|MessageBodyReader
argument_list|<
name|Object
argument_list|>
implements|,
name|MessageBodyWriter
argument_list|<
name|Object
argument_list|>
block|{
specifier|private
specifier|static
name|Map
argument_list|<
name|Class
argument_list|<
name|?
argument_list|>
argument_list|,
name|AegisContext
argument_list|>
name|classContexts
init|=
operator|new
name|WeakHashMap
argument_list|<
name|Class
argument_list|<
name|?
argument_list|>
argument_list|,
name|AegisContext
argument_list|>
argument_list|()
decl_stmt|;
annotation|@
name|Context
specifier|protected
name|ContextResolver
argument_list|<
name|AegisContext
argument_list|>
name|resolver
decl_stmt|;
specifier|public
name|boolean
name|isWriteable
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
name|type
parameter_list|,
name|Type
name|genericType
parameter_list|,
name|Annotation
index|[]
name|anns
parameter_list|,
name|MediaType
name|mt
parameter_list|)
block|{
return|return
name|isSupported
argument_list|(
name|type
argument_list|,
name|genericType
argument_list|,
name|anns
argument_list|)
return|;
block|}
specifier|public
name|boolean
name|isReadable
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
name|type
parameter_list|,
name|Type
name|genericType
parameter_list|,
name|Annotation
index|[]
name|annotations
parameter_list|,
name|MediaType
name|mt
parameter_list|)
block|{
return|return
name|isSupported
argument_list|(
name|type
argument_list|,
name|genericType
argument_list|,
name|annotations
argument_list|)
return|;
block|}
specifier|public
name|long
name|getSize
parameter_list|(
name|Object
name|o
parameter_list|,
name|Class
argument_list|<
name|?
argument_list|>
name|type
parameter_list|,
name|Type
name|genericType
parameter_list|,
name|Annotation
index|[]
name|annotations
parameter_list|,
name|MediaType
name|mt
parameter_list|)
block|{
return|return
operator|-
literal|1
return|;
block|}
specifier|protected
name|AegisContext
name|getAegisContext
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
name|type
parameter_list|,
name|Type
name|genericType
parameter_list|)
block|{
if|if
condition|(
name|resolver
operator|!=
literal|null
condition|)
block|{
name|AegisContext
name|context
init|=
name|resolver
operator|.
name|getContext
argument_list|(
name|type
argument_list|)
decl_stmt|;
comment|// it's up to the resolver to keep its contexts in a map
if|if
condition|(
name|context
operator|!=
literal|null
condition|)
block|{
return|return
name|context
return|;
block|}
block|}
return|return
name|getClassContext
argument_list|(
name|type
argument_list|,
name|genericType
argument_list|)
return|;
block|}
specifier|private
name|void
name|addType
parameter_list|(
name|Set
argument_list|<
name|Class
argument_list|<
name|?
argument_list|>
argument_list|>
name|rootClasses
parameter_list|,
name|Type
name|cls
parameter_list|,
name|boolean
name|allowArray
parameter_list|)
block|{
if|if
condition|(
name|cls
operator|instanceof
name|Class
condition|)
block|{
if|if
condition|(
operator|(
operator|(
name|Class
operator|)
name|cls
operator|)
operator|.
name|isArray
argument_list|()
operator|&&
operator|!
name|allowArray
condition|)
block|{
name|rootClasses
operator|.
name|add
argument_list|(
operator|(
operator|(
name|Class
operator|)
name|cls
operator|)
operator|.
name|getComponentType
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|rootClasses
operator|.
name|add
argument_list|(
operator|(
name|Class
operator|)
name|cls
argument_list|)
expr_stmt|;
block|}
block|}
elseif|else
if|if
condition|(
name|cls
operator|instanceof
name|ParameterizedType
condition|)
block|{
for|for
control|(
name|Type
name|t2
range|:
operator|(
operator|(
name|ParameterizedType
operator|)
name|cls
operator|)
operator|.
name|getActualTypeArguments
argument_list|()
control|)
block|{
name|addType
argument_list|(
name|rootClasses
argument_list|,
name|t2
argument_list|,
literal|false
argument_list|)
expr_stmt|;
block|}
block|}
elseif|else
if|if
condition|(
name|cls
operator|instanceof
name|GenericArrayType
condition|)
block|{
name|GenericArrayType
name|gt
init|=
operator|(
name|GenericArrayType
operator|)
name|cls
decl_stmt|;
name|Class
name|ct
init|=
operator|(
name|Class
operator|)
name|gt
operator|.
name|getGenericComponentType
argument_list|()
decl_stmt|;
name|ct
operator|=
name|Array
operator|.
name|newInstance
argument_list|(
name|ct
argument_list|,
literal|0
argument_list|)
operator|.
name|getClass
argument_list|()
expr_stmt|;
name|rootClasses
operator|.
name|add
argument_list|(
name|ct
argument_list|)
expr_stmt|;
block|}
block|}
specifier|private
name|AegisContext
name|getClassContext
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
name|type
parameter_list|,
name|Type
name|genericType
parameter_list|)
block|{
synchronized|synchronized
init|(
name|classContexts
init|)
block|{
name|AegisContext
name|context
init|=
name|classContexts
operator|.
name|get
argument_list|(
name|type
argument_list|)
decl_stmt|;
if|if
condition|(
name|context
operator|==
literal|null
condition|)
block|{
name|context
operator|=
operator|new
name|AegisContext
argument_list|()
expr_stmt|;
name|context
operator|.
name|setWriteXsiTypes
argument_list|(
literal|true
argument_list|)
expr_stmt|;
comment|// needed, since we know no element/type maps.
name|context
operator|.
name|setReadXsiTypes
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|Set
argument_list|<
name|Class
argument_list|<
name|?
argument_list|>
argument_list|>
name|rootClasses
init|=
operator|new
name|HashSet
argument_list|<
name|Class
argument_list|<
name|?
argument_list|>
argument_list|>
argument_list|()
decl_stmt|;
name|rootClasses
operator|.
name|add
argument_list|(
name|type
argument_list|)
expr_stmt|;
if|if
condition|(
operator|!
operator|(
name|genericType
operator|instanceof
name|Class
operator|)
condition|)
block|{
name|addType
argument_list|(
name|rootClasses
argument_list|,
name|genericType
argument_list|,
literal|true
argument_list|)
expr_stmt|;
block|}
name|context
operator|.
name|setRootClasses
argument_list|(
name|rootClasses
argument_list|)
expr_stmt|;
name|context
operator|.
name|initialize
argument_list|()
expr_stmt|;
name|classContexts
operator|.
name|put
argument_list|(
name|type
argument_list|,
name|context
argument_list|)
expr_stmt|;
block|}
return|return
name|context
return|;
block|}
block|}
comment|/**      * For Aegis, it's not obvious to me how we'd decide that a type was hopeless.      * @param type      * @param genericType      * @param annotations      * @return      */
specifier|protected
name|boolean
name|isSupported
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
name|type
parameter_list|,
name|Type
name|genericType
parameter_list|,
name|Annotation
index|[]
name|annotations
parameter_list|)
block|{
return|return
literal|true
return|;
block|}
block|}
end_class

end_unit

