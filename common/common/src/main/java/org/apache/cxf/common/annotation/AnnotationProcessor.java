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
name|common
operator|.
name|annotation
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
name|Field
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
name|Method
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
name|logging
operator|.
name|Logger
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
name|i18n
operator|.
name|Message
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
name|logging
operator|.
name|LogUtils
import|;
end_import

begin_comment
comment|/** Process instance of an annotated class.  This is a visitable  * object that allows an caller to visit that annotated elements in  * this class definition.  If a class level annotation is overridden  * by a member level annotation, only the visit method for the member  * level annotation  */
end_comment

begin_class
specifier|public
class|class
name|AnnotationProcessor
block|{
specifier|private
specifier|static
specifier|final
name|Logger
name|LOG
init|=
name|LogUtils
operator|.
name|getL7dLogger
argument_list|(
name|AnnotationProcessor
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|private
specifier|final
name|Object
name|target
decl_stmt|;
specifier|private
name|List
argument_list|<
name|Class
argument_list|<
name|?
extends|extends
name|Annotation
argument_list|>
argument_list|>
name|annotationTypes
decl_stmt|;
specifier|public
name|AnnotationProcessor
parameter_list|(
name|Object
name|o
parameter_list|)
block|{
if|if
condition|(
name|o
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
operator|new
name|Message
argument_list|(
literal|"INVALID_CTOR_ARGS"
argument_list|,
name|LOG
argument_list|)
operator|.
name|toString
argument_list|()
argument_list|)
throw|;
block|}
name|target
operator|=
name|o
expr_stmt|;
block|}
comment|/**       * Visits each of the annotated elements of the object.      *       * @param visitor a visitor       * @param claz the Class of the targe object      */
specifier|public
name|void
name|accept
parameter_list|(
name|AnnotationVisitor
name|visitor
parameter_list|,
name|Class
argument_list|<
name|?
argument_list|>
name|claz
parameter_list|)
block|{
if|if
condition|(
name|visitor
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|()
throw|;
block|}
name|annotationTypes
operator|=
name|visitor
operator|.
name|getTargetAnnotations
argument_list|()
expr_stmt|;
name|visitor
operator|.
name|setTarget
argument_list|(
name|target
argument_list|)
expr_stmt|;
comment|//recursively check annotation in super class
name|processClass
argument_list|(
name|visitor
argument_list|,
name|claz
argument_list|)
expr_stmt|;
name|processFields
argument_list|(
name|visitor
argument_list|,
name|claz
argument_list|)
expr_stmt|;
name|processMethods
argument_list|(
name|visitor
argument_list|,
name|claz
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|accept
parameter_list|(
name|AnnotationVisitor
name|visitor
parameter_list|)
block|{
name|accept
argument_list|(
name|visitor
argument_list|,
name|target
operator|.
name|getClass
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|processMethods
parameter_list|(
name|AnnotationVisitor
name|visitor
parameter_list|,
name|Class
argument_list|<
name|?
extends|extends
name|Object
argument_list|>
name|targetClass
parameter_list|)
block|{
if|if
condition|(
name|targetClass
operator|.
name|getSuperclass
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|processMethods
argument_list|(
name|visitor
argument_list|,
name|targetClass
operator|.
name|getSuperclass
argument_list|()
argument_list|)
expr_stmt|;
block|}
for|for
control|(
name|Method
name|element
range|:
name|targetClass
operator|.
name|getDeclaredMethods
argument_list|()
control|)
block|{
for|for
control|(
name|Class
argument_list|<
name|?
extends|extends
name|Annotation
argument_list|>
name|clz
range|:
name|annotationTypes
control|)
block|{
name|Annotation
name|ann
init|=
name|element
operator|.
name|getAnnotation
argument_list|(
name|clz
argument_list|)
decl_stmt|;
if|if
condition|(
name|ann
operator|!=
literal|null
condition|)
block|{
name|visitor
operator|.
name|visitMethod
argument_list|(
name|element
argument_list|,
name|ann
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
specifier|private
name|void
name|processFields
parameter_list|(
name|AnnotationVisitor
name|visitor
parameter_list|,
name|Class
argument_list|<
name|?
extends|extends
name|Object
argument_list|>
name|targetClass
parameter_list|)
block|{
if|if
condition|(
name|targetClass
operator|.
name|getSuperclass
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|processFields
argument_list|(
name|visitor
argument_list|,
name|targetClass
operator|.
name|getSuperclass
argument_list|()
argument_list|)
expr_stmt|;
block|}
for|for
control|(
name|Field
name|element
range|:
name|targetClass
operator|.
name|getDeclaredFields
argument_list|()
control|)
block|{
for|for
control|(
name|Class
argument_list|<
name|?
extends|extends
name|Annotation
argument_list|>
name|clz
range|:
name|annotationTypes
control|)
block|{
name|Annotation
name|ann
init|=
name|element
operator|.
name|getAnnotation
argument_list|(
name|clz
argument_list|)
decl_stmt|;
if|if
condition|(
name|ann
operator|!=
literal|null
condition|)
block|{
name|visitor
operator|.
name|visitField
argument_list|(
name|element
argument_list|,
name|ann
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
specifier|private
name|void
name|processClass
parameter_list|(
name|AnnotationVisitor
name|visitor
parameter_list|,
name|Class
argument_list|<
name|?
extends|extends
name|Object
argument_list|>
name|targetClass
parameter_list|)
block|{
if|if
condition|(
name|targetClass
operator|.
name|getSuperclass
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|processClass
argument_list|(
name|visitor
argument_list|,
name|targetClass
operator|.
name|getSuperclass
argument_list|()
argument_list|)
expr_stmt|;
block|}
for|for
control|(
name|Class
argument_list|<
name|?
extends|extends
name|Annotation
argument_list|>
name|clz
range|:
name|annotationTypes
control|)
block|{
name|Annotation
name|ann
init|=
name|targetClass
operator|.
name|getAnnotation
argument_list|(
name|clz
argument_list|)
decl_stmt|;
if|if
condition|(
name|ann
operator|!=
literal|null
condition|)
block|{
name|visitor
operator|.
name|visitClass
argument_list|(
name|targetClass
argument_list|,
name|ann
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
end_class

end_unit

