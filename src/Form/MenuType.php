<?php

namespace App\Form;

use App\Entity\Menu;
use App\Entity\Burger;
use App\Entity\Complement;
use App\Repository\BurgerRepository;
use App\Repository\ComplementRepository;
use Symfony\Component\Form\AbstractType;
use Symfony\Component\Form\FormBuilderInterface;
use Symfony\Component\OptionsResolver\OptionsResolver;
use Symfony\Bridge\Doctrine\Form\Type\EntityType;
use Symfony\Component\Form\Extension\Core\Type\TextType;
use Symfony\Component\Form\Extension\Core\Type\NumberType;
use Symfony\Component\Form\Extension\Core\Type\FileType;
use Symfony\Component\Validator\Constraints\File;

class MenuType extends AbstractType
{
    public function buildForm(FormBuilderInterface $builder, array $options): void
    {
        $builder
            ->add('nom', TextType::class, [
                'label' => 'Nom du menu',
                'attr' => ['placeholder' => 'Ex: Menu Classic']
            ])
            ->add('burger', EntityType::class, [
                'label' => 'Burger',
                'class' => Burger::class,
                'choice_label' => 'nom',
                'placeholder' => 'Sélectionnez un burger',
                'query_builder' => function (BurgerRepository $er) {
                    return $er->createQueryBuilder('b')
                        ->where('b.archived = false')
                        ->orderBy('b.nom', 'ASC');
                }
            ])
            ->add('frite', EntityType::class, [
                'label' => 'Frites',
                'class' => Complement::class,
                'choice_label' => 'nom',
                'required' => false,
                'placeholder' => 'Sélectionnez des frites (optionnel)',
                'query_builder' => function (ComplementRepository $er) {
                    return $er->createQueryBuilder('c')
                        ->where('c.type = :type')
                        ->andWhere('c.archived = false')
                        ->setParameter('type', 'FRITE')
                        ->orderBy('c.nom', 'ASC');
                }
            ])
            ->add('boisson', EntityType::class, [
                'label' => 'Boisson',
                'class' => Complement::class,
                'choice_label' => 'nom',
                'required' => false,
                'placeholder' => 'Sélectionnez une boisson (optionnel)',
                'query_builder' => function (ComplementRepository $er) {
                    return $er->createQueryBuilder('c')
                        ->where('c.type = :type')
                        ->andWhere('c.archived = false')
                        ->setParameter('type', 'BOISSON')
                        ->orderBy('c.nom', 'ASC');
                }
            ])
            ->add('imageFile', FileType::class, [
                'label' => 'Image du menu',
                'required' => false,
                'mapped' => false,
                'constraints' => [
                    new File([
                        'maxSize' => '2M',
                        'mimeTypes' => ['image/jpeg', 'image/png', 'image/webp'],
                        'mimeTypesMessage' => 'Veuillez uploader une image valide (JPEG, PNG ou WebP)',
                    ])
                ],
                'attr' => ['accept' => 'image/*']
            ]);
    }

    public function configureOptions(OptionsResolver $resolver): void
    {
        $resolver->setDefaults([
            'data_class' => Menu::class,
        ]);
    }
}