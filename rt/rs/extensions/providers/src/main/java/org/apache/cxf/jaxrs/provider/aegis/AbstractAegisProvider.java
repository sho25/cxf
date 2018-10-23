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
operator|.
name|aegis
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
parameter_list|<
name|T
parameter_list|>
implements|implements
name|MessageBodyReader
argument_list|<
name|T
argument_list|>
implements|,
name|MessageBodyWriter
argument_list|<
name|T
argument_list|>
block|{
specifier|private
specifier|static
name|Map
argument_list|<
name|java
operator|.
name|lang
operator|.
name|reflect
operator|.
name|Type
argument_list|,
name|AegisContext
argument_list|>
name|classContexts
init|=
operator|new
name|WeakHashMap
argument_list|<
name|java
operator|.
name|lang
operator|.
name|reflect
operator|.
name|Type
argument_list|,
name|AegisContext
argument_list|>
argument_list|()
decl_stmt|;
specifier|protected
name|boolean
name|writeXsiType
init|=
literal|true
decl_stmt|;
specifier|protected
name|boolean
name|readXsiType
init|=
literal|true
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
name|void
name|setWriteXsiType
parameter_list|(
name|boolean
name|write
parameter_list|)
block|{
name|writeXsiType
operator|=
name|write
expr_stmt|;
block|}
specifier|public
name|void
name|setReadXsiType
parameter_list|(
name|boolean
name|read
parameter_list|)
block|{
name|readXsiType
operator|=
name|read
expr_stmt|;
block|}
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
name|T
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
name|plainClass
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
comment|/* wierdly, the JAX-RS API keys on Class, not AegisType, so it can't possibly              * keep generics straight. Should we ignore the resolver?              */
name|AegisContext
name|context
init|=
name|resolver
operator|.
name|getContext
argument_list|(
name|plainClass
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
if|if
condition|(
name|genericType
operator|==
literal|null
condition|)
block|{
name|genericType
operator|=
name|plainClass
expr_stmt|;
block|}
return|return
name|getClassContext
argument_list|(
name|genericType
argument_list|)
return|;
block|}
specifier|private
name|AegisContext
name|getClassContext
parameter_list|(
name|Type
name|reflectionType
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
name|reflectionType
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
name|writeXsiType
argument_list|)
expr_stmt|;
name|context
operator|.
name|setReadXsiTypes
argument_list|(
name|readXsiType
argument_list|)
expr_stmt|;
name|Set
argument_list|<
name|java
operator|.
name|lang
operator|.
name|reflect
operator|.
name|Type
argument_list|>
name|rootClasses
init|=
operator|new
name|HashSet
argument_list|<>
argument_list|()
decl_stmt|;
name|rootClasses
operator|.
name|add
argument_list|(
name|reflectionType
argument_list|)
expr_stmt|;
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
name|reflectionType
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
specifier|static
name|void
name|clearContexts
parameter_list|()
block|{
name|classContexts
operator|.
name|clear
argument_list|()
expr_stmt|;
block|}
block|}
end_class

end_unit

