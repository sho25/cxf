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

begin_comment
comment|/** Visits the annotated elements of an object  *  */
end_comment

begin_interface
specifier|public
interface|interface
name|AnnotationVisitor
block|{
comment|/** set the target object being visited.  Invoked before any of      * the visit methods.         *      * @see AnnotationProcessor      *      * @param target the target object       */
name|void
name|setTarget
parameter_list|(
name|Object
name|target
parameter_list|)
function_decl|;
comment|/** return the list of annotations this visitor wants to be      * informed about.      *      * @return list of annotation types to be informed about      *      */
name|List
argument_list|<
name|Class
argument_list|<
name|?
extends|extends
name|Annotation
argument_list|>
argument_list|>
name|getTargetAnnotations
parameter_list|()
function_decl|;
comment|/** visit an annotated class. Invoked when the class of an object      * is annotated by one of the specified annotations.      *<code>visitClass</code> is called for each of the annotations      * that matches and for each class.       *      * @param clz the class with the annotation      * @param annotation the annotation       *      */
name|void
name|visitClass
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
name|clz
parameter_list|,
name|Annotation
name|annotation
parameter_list|)
function_decl|;
comment|/** visit an annotated field. Invoked when the field of an object      * is annotated by one of the specified annotations.      *<code>visitField</code> is called for each of the annotations      * that matches and for each field.       *      * @param field the annotated field      * @param annotation the annotation       *      */
name|void
name|visitField
parameter_list|(
name|Field
name|field
parameter_list|,
name|Annotation
name|annotation
parameter_list|)
function_decl|;
comment|/** visit an annotated method. Invoked when the method of an object      * is annotated by one of the specified annotations.      *<code>visitMethod</code> is called for each of the annotations      * that matches and for each method.       *      * @param method the annotated fieldx      * @param annotation the annotation       *      */
name|void
name|visitMethod
parameter_list|(
name|Method
name|method
parameter_list|,
name|Annotation
name|annotation
parameter_list|)
function_decl|;
block|}
end_interface

end_unit

